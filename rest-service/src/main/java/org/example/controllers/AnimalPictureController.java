package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.interfaces.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/getPicture")
@RestController
public class AnimalPictureController {
    private final AnimalService animalService;

    @GetMapping("/cat")
    public ResponseEntity<?> getLinkCatPicture() {
        String urlCatPicture = animalService.getUrlCatPicture();
        return ResponseEntity.
                ok(urlCatPicture);
    }
    @GetMapping("/cat-gif")
    public ResponseEntity<?> getLinkCatGif() {
        String urlCatGif = animalService.getUrlCatGif();
        return ResponseEntity.
                ok(urlCatGif);
    }
}
