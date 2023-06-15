package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.EncryptionTool;
import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.example.models.BinaryContent;
import org.example.repositories.ApplicationDocumentRepository;
import org.example.repositories.ApplicationPhotoRepository;
import org.example.services.interfaces.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final ApplicationPhotoRepository applicationPhotoRepository;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final EncryptionTool encryptionTool;

    @Transactional(readOnly = true)
    @Override
    public Optional<ApplicationDocument> getDocument(String id) {
        long l = encryptionTool.hashOff(id);
        return applicationDocumentRepository.findById(l);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ApplicationPhoto> getPhoto(String id) {
        long l = encryptionTool.hashOff(id);
        return applicationPhotoRepository.findById(l);
    }

    @Override
    public ResponseEntity<?> uploadDoc(ApplicationDocument applicationDocument) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(applicationDocument.getMimeType()));
            BinaryContent binaryContentNew = applicationDocument.getBinaryContent();
            return new ResponseEntity<>(binaryContentNew.getFileAsArrayOfBytes(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ошибка при загрузке документа c id {}", applicationDocument.getId(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось загрузить документ", e);
        }
    }

    @Override
    public ResponseEntity<?> uploadPhoto(ApplicationPhoto applicationPhoto) {
        String randomPhotoName = RandomStringUtils.randomAlphanumeric(11, 11);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", randomPhotoName + ".jpg");
            BinaryContent binaryContentNew = applicationPhoto.getBinaryContent();
            return new ResponseEntity<>(binaryContentNew.getFileAsArrayOfBytes(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ошибка при загрузке документа c id {}", applicationPhoto.getId(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось загрузить документ", e);
        }
    }
}
