package org.example.service.interfaces;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface FileService {
    Optional<ApplicationDocument> getDocument(String id);
    Optional<ApplicationPhoto> getPhoto(String id);

    void uploadDoc(ApplicationDocument applicationDocument, HttpServletResponse response);
    void uploadPhoto(ApplicationPhoto applicationPhoto, HttpServletResponse response);

}
