package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.EncryptionTool;
import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.example.models.BinaryContent;
import org.example.models.enums.TypeFile;
import org.example.repositories.ApplicationDocumentRepository;
import org.example.repositories.ApplicationPhotoRepository;
import org.example.services.interfaces.FileService;
import org.example.util.BinaryContentIterator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Iterator;
import java.util.Optional;

import static org.example.models.enums.TypeFile.DOCUMENT;
import static org.example.models.enums.TypeFile.PHOTO;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.parseMediaType;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final ApplicationPhotoRepository applicationPhotoRepository;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final EncryptionTool encryptionTool;
    private final EntityManagerFactory entityManagerFactory;
    private final BinaryContentIterator binaryContentIterator;

    @Transactional(readOnly = true)
    @Override
    public Optional<ApplicationDocument> getDocument(String id) {
        long l;
        try {
            l = encryptionTool.hashOff(id);
        } catch (Exception e) {
            log.error("Полученный id не удалось расшифровать", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Полученный id не удалось расшифровать");
        }
        return applicationDocumentRepository.findById(l);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ApplicationPhoto> getPhoto(String id) {
        long l;
        try {
            l = encryptionTool.hashOff(id);
        } catch (Exception e) {
            log.error("Полученный id не удалось расшифровать", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Полученный id не удалось расшифровать");
        }
        return applicationPhotoRepository.findById(l);
    }


    @Override
    public ResponseEntity<StreamingResponseBody> uploadDoc(ApplicationDocument applicationDocument) {
        return uploadFile(applicationDocument.getId(), applicationDocument.getMimeType(), applicationDocument.getDocName(), DOCUMENT);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> uploadPhoto(ApplicationPhoto applicationPhoto) {
        String randomPhotoName = RandomStringUtils.randomAlphanumeric(11, 11) + ".jpg";
        return uploadFile(applicationPhoto.getId(), IMAGE_JPEG.toString(), randomPhotoName, PHOTO);
    }

    private ResponseEntity<StreamingResponseBody> uploadFile(Long file_id, String mediaType, String name_file, TypeFile type_file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(parseMediaType(mediaType));
            headers.setContentDispositionFormData("attachment", name_file);
            StreamingResponseBody responseBody = outputStream -> {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                Iterator<BinaryContent> iterator = binaryContentIterator.iterateBinaryContents(entityManager, file_id, type_file);
                while (iterator.hasNext()) {
                    BinaryContent binaryContent = iterator.next();
                    byte[] fileData = binaryContent.getFileAsArrayOfBytes();
                    outputStream.write(fileData);
                }
            };
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Не удалось загрузить файл c id {}", file_id);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось загрузить файл", e);
        }
    }
}
