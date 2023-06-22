package org.example.services.interfaces;

import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Optional;

public interface FileService {
    Optional<ApplicationDocument> getDocument(String id);

    Optional<ApplicationPhoto> getPhoto(String id);

    ResponseEntity<StreamingResponseBody> uploadDoc(ApplicationDocument applicationDocument);

    ResponseEntity<StreamingResponseBody> uploadPhoto(ApplicationPhoto applicationPhoto);

}
