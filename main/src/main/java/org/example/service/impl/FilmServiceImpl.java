package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Top250FilmsModel;
import org.example.model.Top250SeriesModel;
import org.example.repository.Top250FilmsRepository;
import org.example.repository.Top250SeriesRepository;
import org.example.service.interfaces.FilmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final Top250FilmsRepository filmsRepository;
    private final Top250SeriesRepository seriesRepository;
    private final RestTemplate restTemplate;

    @Value("${link.imdb}")
    private String linkImdb;
    @Value("${link.address.film}")
    private String linkRestFilm;
    @Value("${link.address.series}")
    private String linkRestSeries;

    @Transactional
    @Override
    public String getLinkForFilm() {
        int randomInt = getRandomInt();
        Optional<Top250FilmsModel> optionalFilm = filmsRepository.findById(randomInt);
        if (optionalFilm.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    linkRestFilm,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                optionalFilm = filmsRepository.findById(randomInt);
                if (optionalFilm.isEmpty()) {
                    log.error("Не получилось получить фильм после повторной загрузки в базу");
                    return "Извините, этот сервис временно не доступен, повторите попытку позже";
                }
                return linkImdb + optionalFilm.get().getIdFilm() + "/ \uD83C\uDF55";
            }
            log.error("Ошибка при запросе к рест сервису для загрузки фильма код {} сообщение {}", response.getStatusCode(), response.getBody());
            return "Извините, этот сервис временно не доступен, повторите попытку позже";
        } else {
            return linkImdb + optionalFilm.get().getIdFilm() + "/ \uD83C\uDF55";
        }
    }

    @Transactional
    @Override
    public String getLinkForSeries() {
        int randomInt = getRandomInt();
        Optional<Top250SeriesModel> optionalSeries = seriesRepository.findById(randomInt);
        if (optionalSeries.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    linkRestSeries,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                optionalSeries = seriesRepository.findById(randomInt);
                if (optionalSeries.isEmpty()) {
                    log.error("Не получилось получить сериал после повторной загрузки в базу");
                    return "Извините, этот сервис временно не доступен, повторите попытку позже";
                }
                return linkImdb + optionalSeries.get().getIdSeries() + "/ \uD83C\uDF55";
            }
            log.error("Ошибка при запросе к рест сервису для загрузки сериалов код {} сообщение {}", response.getStatusCode(), response.getBody());
            return "Извините, этот сервис временно не доступен, повторите попытку позже";
        } else {
            return linkImdb + optionalSeries.get().getIdSeries() + "/ \uD83C\uDF55";
        }
    }

    private int getRandomInt() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(1, 251);
    }
}
