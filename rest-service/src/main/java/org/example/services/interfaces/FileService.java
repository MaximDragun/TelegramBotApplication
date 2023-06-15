package org.example.services.interfaces;

import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FileService {
    Optional<ApplicationDocument> getDocument(String id);

    Optional<ApplicationPhoto> getPhoto(String id);

    ResponseEntity<?> uploadDoc(ApplicationDocument applicationDocument);

    ResponseEntity<?> uploadPhoto(ApplicationPhoto applicationPhoto);

}
