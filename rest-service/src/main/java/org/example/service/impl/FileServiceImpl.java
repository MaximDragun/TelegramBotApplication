package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.EncryptionTool;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.repository.ApplicationDocumentRepository;
import org.example.repository.ApplicationPhotoRepository;
import org.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

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
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            File tempFile = File.createTempFile("tempFile", ".bin");
            tempFile.deleteOnExit();
            FileUtils.writeByteArrayToFile(tempFile, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(tempFile);
        } catch (IOException e) {
            log.error("Ooops", e);
            return null;
        }


    }
}
