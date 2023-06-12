package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionTool;
import org.example.exceptions.ExceededMaxSize;
import org.example.exceptions.UploadFileException;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.repository.ApplicationDocumentRepository;
import org.example.repository.ApplicationPhotoRepository;
import org.example.repository.BinaryContentRepository;
import org.example.service.enums.LinkType;
import org.example.service.interfaces.FileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    @Value("${token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;
    @Value("${link.address}")
    private String linkAddress;

    private final EncryptionTool encryptionTool;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final ApplicationPhotoRepository applicationPhotoRepository;
    private final BinaryContentRepository binaryContentDAO;
    private final RestTemplate restTemplate;
    @Lazy
    @Autowired
    private FileService fileService;

    @Transactional
    @Override
    public ApplicationDocument processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        Long fileSize = telegramDoc.getFileSize();
        if (((fileSize / 1024.0) / 1024.0) > 10)
            throw new ExceededMaxSize();

        String fileId = telegramDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = fileService.getPersistentBinaryContent(response);
            ApplicationDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return applicationDocumentRepository.save(transientAppDoc);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Transactional
    @Override
    public ApplicationPhoto processPhoto(Message telegramMessage) {
        PhotoSize telegramPhoto = telegramMessage.getPhoto().get(telegramMessage.getPhoto().size() - 1);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = fileService.getPersistentBinaryContent(response);
            ApplicationPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return applicationPhotoRepository.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public String genericLink(Long fileId, LinkType linkType) {
        String hashLink = encryptionTool.hashOn(fileId);
        return "[click me](" + linkAddress + "/" + linkType + "?id=" + hashLink + ")";
    }

    @Transactional
    public BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        return binaryContentDAO.save(transientBinaryContent);
    }

    private static String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private ApplicationPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return ApplicationPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    private ApplicationDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return ApplicationDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
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

    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        HttpURLConnection connection;
        try {
            URL url = new URL(fullUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
        } catch (IOException e) {
            log.error("Ошибка во время подключения к URL {}", fullUri, e);
            throw new UploadFileException(e);
        }

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    return outputStream.toByteArray();
                }

            } else {
                log.error("Загрузить файл из базы телеграма по uri {} не удалось", fullUri);
                throw new UploadFileException();
            }

        } catch (IOException e) {
            log.error("Загрузить файл из базы телеграма по uri {} не удалось", fullUri, e);
            throw new UploadFileException(e);
        } finally {
            connection.disconnect();
        }
    }
}
