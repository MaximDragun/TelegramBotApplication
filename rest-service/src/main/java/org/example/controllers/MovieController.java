package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.interfaces.UploadContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/imdb")
@RestController
public class MovieController {
    private final UploadContentService uploadContentService;

    @GetMapping("/film")
    public ResponseEntity<?> uploadFilms() {
        uploadContentService.uploadFilms();
        return ResponseEntity.
                ok()
                .build();
    }

    @GetMapping("/series")
    public ResponseEntity<?> uploadSeries() {
        uploadContentService.uploadSeries();
        return ResponseEntity.
                ok()
                .build();
    }
}
