package org.example.service;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.springframework.core.io.FileSystemResource;

import java.util.Optional;

public interface FileService {
    Optional<ApplicationDocument> getDocument(String id);
    Optional<ApplicationPhoto> getPhoto(String id);
}
