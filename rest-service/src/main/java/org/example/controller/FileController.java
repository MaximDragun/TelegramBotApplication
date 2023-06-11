package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.service.interfaces.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/download_file")
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-document")
    public ResponseEntity<?> getDocument(@RequestParam("id") String id) {
        Optional<ApplicationDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            throw new NotFoundException("Документ не найден. Попробуйте снова");
        }
        return fileService.uploadDoc(doc.get());
    }

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        Optional<ApplicationPhoto> photo = fileService.getPhoto(id);
        log.error("Я нахожусь в контроллере реста, метод get_photo, Получил фото из бд");
        if (photo.isEmpty()) {
            throw new NotFoundException("Фото не найдено. Попробуйте снова");
        }
        return fileService.uploadPhoto(photo.get());
    }
}

