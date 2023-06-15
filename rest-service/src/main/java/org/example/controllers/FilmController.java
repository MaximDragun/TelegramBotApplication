package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.interfaces.UploadFilmService;
import org.example.services.interfaces.UploadSeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/imdb")
@RestController
public class FilmController {
    private final UploadSeriesService uploadSeriesService;
    private final UploadFilmService uploadFilmService;


    @GetMapping("/film")
    public ResponseEntity<?> uploadFilms() {
        uploadFilmService.uploadFilms();
        return ResponseEntity.
                ok()
                .build();
    }

    @GetMapping("/series")
    public ResponseEntity<?> uploadSeries() {
        uploadSeriesService.uploadSeries();
        return ResponseEntity.
                ok()
                .build();
    }
}
