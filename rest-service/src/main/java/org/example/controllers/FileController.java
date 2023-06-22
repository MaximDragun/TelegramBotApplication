package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.NotFoundException;
import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.example.services.interfaces.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/download_file")
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-document")
    public ResponseEntity<StreamingResponseBody> getDocument(@RequestParam("id") String id) {
        Optional<ApplicationDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            throw new NotFoundException("Документ не найден. Попробуйте снова");
        }
        return fileService.uploadDoc(doc.get());
    }

    @GetMapping("/get-photo")
    public ResponseEntity<StreamingResponseBody> getPhoto(@RequestParam("id") String id) {
        Optional<ApplicationPhoto> photo = fileService.getPhoto(id);
        if (photo.isEmpty()) {
            throw new NotFoundException("Фото не найдено. Попробуйте снова");
        }
        return fileService.uploadPhoto(photo.get());
    }
}

