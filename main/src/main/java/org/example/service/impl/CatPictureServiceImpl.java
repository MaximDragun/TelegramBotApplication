package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.interfaces.CatPictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatPictureServiceImpl implements CatPictureService {
    private final RestTemplate restTemplate;

    @Value("${url.catapi.restservice}")
    private String restCatUrl;
    @Value("${url.catapi.restservice.gif}")
    private String restCatGifUrl;

    @Override
    public String getLinkCatPicture() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                restCatUrl,
                HttpMethod.GET,
                request,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Ошибка при обращении к restservice для получения ссылки на рандомное фото" +
                    " статус {} ошибка {}", response.getStatusCode(), response.getBody());
            return "Ошибка запроса, повторите запрос позже";
        }

    }

    @Override
    public String getLinkCatGif() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                restCatGifUrl,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Ошибка при обращении к restservice для получения ссылки на рандомную гифку" +
                    " статус {} ошибка {}", response.getStatusCode(), response.getBody());
            return "Ошибка запроса, повторите запрос позже";
        }
    }
}
