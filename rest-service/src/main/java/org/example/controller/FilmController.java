package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.impl.UploadFilmServiceImpl;
import org.example.service.impl.UploadSeriesServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/imdb")
@RestController
public class FilmController {
 private final    UploadSeriesServiceImpl uploadSeriesService;
 private final UploadFilmServiceImpl uploadFilmService;


    @GetMapping("/film")
    public ResponseEntity<?> uploadFilms(){
        uploadFilmService.uploadFilms();
        return ResponseEntity.
                ok()
                .build();
    }
    @GetMapping("/series")
    public ResponseEntity<?> uploadSeries(){
        uploadSeriesService.uploadSeries();
        return ResponseEntity.
                ok()
                .build();
    }
}
