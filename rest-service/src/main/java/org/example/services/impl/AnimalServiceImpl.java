package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.RandomCatDTO;
import org.example.services.interfaces.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final RestTemplate restTemplate;

    @Value("${cat.api.url}")
    private String catUrl;
    @Value("${cat.api.url.json}")
    private String catUrlRequestPicture;
    @Value("${cat.api.url.json.gif}")
    private String catUrlRequestGif;

    @Override
    public String getUrlCatPicture() {
        return getUrlResult(catUrlRequestPicture);
    }

    @Override
    public String getUrlCatGif() {
        return getUrlResult(catUrlRequestGif);
    }

    private String getUrlResult(String catUrlRequest) {
        ResponseEntity<RandomCatDTO> response = restTemplate.getForEntity(catUrlRequest, RandomCatDTO.class);
        if (response.getBody() == null || response.getBody().getUrl() == null)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ответ от cat api не валиден");
        return catUrl + response.getBody().getUrl();
    }
}
