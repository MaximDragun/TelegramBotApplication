package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Top250Data;
import org.example.dto.Top250DataDetail;
import org.example.models.Top250FilmsModel;
import org.example.models.Top250SeriesModel;
import org.example.repositories.Top250FilmsRepository;
import org.example.repositories.Top250SeriesRepository;
import org.example.services.interfaces.UploadContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadContentServiceImpl implements UploadContentService {
    private final Top250FilmsRepository filmsRepository;
    private final Top250SeriesRepository seriesRepository;
    private final RestTemplate restTemplate;

    @Value("${imdb.series.uri}")
    private String seriesUri;
    @Value("${imdb.film.uri}")
    private String filmUri;
    @Value("${imdb-api.token}")
    private String imdbApiToken;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void uploadFilms() {
        if (filmsRepository.findById(1).isPresent()) return;
        Top250Data top250Data = restTemplate.getForObject(filmUri, Top250Data.class, imdbApiToken);
        if (top250Data == null) {
            log.error("Не удалось получить список фильмов от imdb ip");
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Не удалось получить список фильмов от imdb ip");
        }
        if (!top250Data.getErrorMessage().isEmpty()) {
            log.error("Ошибка при получении списка фильмов от imdb api {}", top250Data.getErrorMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, top250Data.getErrorMessage());
        }
        for (Top250DataDetail item : top250Data.getItems()) {
            Top250FilmsModel top250FilmsModel = Top250FilmsModel.builder().idFilm(item.getId()).build();
            filmsRepository.save(top250FilmsModel);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void uploadSeries() {
        if (seriesRepository.findById(1).isPresent()) return;
        Top250Data top250Data = restTemplate.getForObject(seriesUri, Top250Data.class, imdbApiToken);
        if (top250Data == null) {
            log.error("Не удалось получить список сериалов от imdb ip");
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Не удалось получить список сериалов от imdb ip");
        }
        if (!top250Data.getErrorMessage().isEmpty()) {
            log.error("Ошибка при получении списка сериалов от imdb api {}", top250Data.getErrorMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, top250Data.getErrorMessage());
        }
        for (Top250DataDetail item : top250Data.getItems()) {
            Top250SeriesModel top250SeriesModel = Top250SeriesModel.builder().idSeries(item.getId()).build();
            seriesRepository.save(top250SeriesModel);
        }
    }
}
