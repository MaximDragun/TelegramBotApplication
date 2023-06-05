package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.NotFoundException;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.service.interfaces.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/download_file")
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-document")
    public void getDocument(@RequestParam("id") String id, HttpServletResponse response) {
        Optional<ApplicationDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            throw new NotFoundException("Документ не найден. Попробуйте снова");
        }
        fileService.uploadDoc(doc.get(), response);
    }

    @GetMapping("/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        Optional<ApplicationPhoto> photo = fileService.getPhoto(id);
        if (photo.isEmpty()) {
            throw new NotFoundException("Фото не найдено. Попробуйте снова");
        }
        fileService.uploadPhoto(photo.get(), response);
    }
}

