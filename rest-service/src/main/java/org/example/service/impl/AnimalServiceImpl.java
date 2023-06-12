package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.RandomCatDTO;
import org.example.service.interfaces.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

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

    @SneakyThrows
    @Override
    public String getUrlCatPicture() {
        ResponseEntity<RandomCatDTO> response = null;
        int i = 0;
        while (response == null || response.getStatusCode() != HttpStatus.OK) {
            try {
                response = restTemplate.getForEntity(catUrlRequestPicture, RandomCatDTO.class);
            } catch (RestClientException e) {
                i++;
                if (i > 30) {
                    log.error("Не удалось получить ответ с cat api picture");
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось обработать ответ сервера");
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
        if (response.getBody() == null || response.getBody().getUrl() == null)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ответ от cat api не валиден");
        return catUrl + response.getBody().getUrl();
    }

    @SneakyThrows
    @Override
    public String getUrlCatGif() {
        ResponseEntity<RandomCatDTO> response = null;
        int i = 0;
        while (response == null || response.getStatusCode() != HttpStatus.OK) {
            try {
                response = restTemplate.getForEntity(catUrlRequestGif, RandomCatDTO.class);
            } catch (RestClientException e) {
                i++;
                if (i > 30) {
                    log.error("Не удалось получить ответ с cat api gif");
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось обработать ответ сервера");
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
        if (response.getBody() == null || response.getBody().getUrl() == null)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ответ от cat api не валиден");
        return catUrl + response.getBody().getUrl();
    }
}
