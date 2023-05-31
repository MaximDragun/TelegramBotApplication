package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.EncryptionTool;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.repository.ApplicationDocumentRepository;
import org.example.repository.ApplicationPhotoRepository;
import org.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final ApplicationPhotoRepository applicationPhotoRepository;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final EncryptionTool encryptionTool;

    @Override
    public Optional<ApplicationDocument> getDocument(String id) {
        long l = encryptionTool.hashOff(id);
        return applicationDocumentRepository.findById(l);
    }

    @Override
    public Optional<ApplicationPhoto> getPhoto(String id) {
        long l = encryptionTool.hashOff(id);
        return applicationPhotoRepository.findById(l);
    }

    @Override
    public void uploadDoc(ApplicationDocument applicationDocument, HttpServletResponse response) {
        response.setContentType(MediaType.parseMediaType(applicationDocument.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + applicationDocument.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);
        BinaryContent binaryContent = applicationDocument.getBinaryContent();
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось загрузить документ", e);
        }
    }

    @Override
    public void uploadPhoto(ApplicationPhoto applicationPhoto, HttpServletResponse response) {
        String randomPhotoName = RandomStringUtils.randomAlphanumeric(11, 11);
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment; filename=" + randomPhotoName + ".jpg");
        response.setStatus(HttpServletResponse.SC_OK);
        BinaryContent binaryContent = applicationPhoto.getBinaryContent();
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось загрузить фото", e);
        }
    }
}
