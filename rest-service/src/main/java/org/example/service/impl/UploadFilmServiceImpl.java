package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.Top250Data;
import org.example.dto.Top250DataDetail;
import org.example.model.Top250FilmsModel;
import org.example.repository.Top250FilmsRepository;
import org.example.service.UploadFilmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UploadFilmServiceImpl implements UploadFilmService {
    private final Top250FilmsRepository filmsRepository;
    private final RestTemplate restTemplate;
    @Value("${imdb.film.uri}")
    private String filmUri;

    @Override
    public void uploadFilms() {
        Top250Data top250Data = restTemplate.getForObject(filmUri, Top250Data.class);
        if (top250Data == null)
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Не удалось получить список фильмов от imdb ip");
        if (!top250Data.getErrorMessage().isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, top250Data.getErrorMessage());
        for (Top250DataDetail item : top250Data.getItems()) {
            Top250FilmsModel top250FilmsModel =  Top250FilmsModel.builder().idFilm(item.getId()).build();
            filmsRepository.save(top250FilmsModel);
        }
    }
}
