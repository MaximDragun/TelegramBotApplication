package org.example.service.interfaces;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FileService {
    Optional<ApplicationDocument> getDocument(String id);

    Optional<ApplicationPhoto> getPhoto(String id);

    ResponseEntity<?> uploadDoc(ApplicationDocument applicationDocument);

    ResponseEntity<?> uploadPhoto(ApplicationPhoto applicationPhoto);

}
