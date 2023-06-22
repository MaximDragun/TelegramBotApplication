package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionTool;
import org.example.exceptions.UploadFileException;
import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.example.models.BinaryContent;
import org.example.models.enums.TypeFile;
import org.example.repositories.ApplicationDocumentRepository;
import org.example.repositories.ApplicationPhotoRepository;
import org.example.repositories.BinaryContentRepository;
import org.example.services.enums.LinkType;
import org.example.services.interfaces.FileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static org.example.models.enums.TypeFile.DOCUMENT;
import static org.example.models.enums.TypeFile.PHOTO;
import static org.example.services.enums.LinkType.GET_DOC;
import static org.example.services.enums.LinkType.GET_PHOTO;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    private final EncryptionTool encryptionTool;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final ApplicationPhotoRepository applicationPhotoRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final RestTemplate restTemplate;

    @Value("${token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;
    @Value("${link.address}")
    private String linkAddress;

    @Transactional
    @Override
    public String processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        String fileId = telegramDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getFilePath(response);
            ApplicationDocument applicationDocument = buildPersistAppDoc(telegramDoc);
            downloadFileAndSaveToDatabase(applicationDocument.getId(), filePath, DOCUMENT);
            return genericLink(applicationDocument.getId(), GET_DOC);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Transactional
    @Override
    public String processPhoto(Message telegramMessage) {
        PhotoSize telegramPhoto = telegramMessage.getPhoto().get(telegramMessage.getPhoto().size() - 1);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getFilePath(response);
            ApplicationPhoto applicationPhoto = buildPersistAppPhoto(telegramPhoto);
            downloadFileAndSaveToDatabase(applicationPhoto.getId(), filePath, PHOTO);
            return genericLink(applicationPhoto.getId(), GET_PHOTO);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    private String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private ApplicationPhoto buildPersistAppPhoto(PhotoSize telegramPhoto) {
        ApplicationPhoto applicationPhoto = ApplicationPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .fileSize(telegramPhoto.getFileSize())
                .build();
        return applicationPhotoRepository.save(applicationPhoto);
    }

    private ApplicationDocument buildPersistAppDoc(Document telegramDoc) {
        ApplicationDocument applicationDocument = ApplicationDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
        return applicationDocumentRepository.save(applicationDocument);
    }

    private void downloadFileAndSaveToDatabase(Long fileId, String filePath, TypeFile typeFile) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        int CHUNK_SIZE = 16 * 1024;
        HttpURLConnection connection = null;
        byte[] buffer = new byte[CHUNK_SIZE];
        try {
            URL url = new URL(fullUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        saveChunkToDatabaseDoc(buffer, bytesRead, fileId, typeFile);
                    }
                }
            } else {
                log.error("Ошибка при получении HttpURLConnection при загрузке в базу данных файла с id {} и типом {}"
                        , fileId, typeFile);
            }
        } catch (IOException e) {
            log.error("Ошибка загрузки в базу данных файла с id {} и типом {}"
                    , fileId, typeFile);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void saveChunkToDatabaseDoc(byte[] chunk, int length, Long file_id, TypeFile typeFile) {
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(Arrays.copyOf(chunk, length))
                .fileId(file_id)
                .typeFile(typeFile)
                .build();
        binaryContentRepository.save(transientBinaryContent);
    }

    private String genericLink(Long fileId, LinkType linkType) {
        String hashLink = encryptionTool.hashOn(fileId);
        return "[click me](" + linkAddress + "/" + linkType + "?id=" + hashLink + ")";
    }
}
