package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.Top250Data;
import org.example.dto.Top250DataDetail;
import org.example.models.Top250FilmsModel;
import org.example.repositories.Top250FilmsRepository;
import org.example.services.interfaces.UploadFilmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UploadFilmServiceImpl implements UploadFilmService {
    private final Top250FilmsRepository filmsRepository;
    private final RestTemplate restTemplate;
    @Value("${imdb.film.uri}")
    private String filmUri;
    @Value("${imdb-api.token}")
    private String imdbApiToken;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void uploadFilms() {
        if (filmsRepository.findById(1).isPresent()) return;
        Top250Data top250Data = restTemplate.getForObject(filmUri, Top250Data.class, imdbApiToken);
        if (top250Data == null)
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Не удалось получить список фильмов от imdb ip");
        if (!top250Data.getErrorMessage().isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, top250Data.getErrorMessage());
        for (Top250DataDetail item : top250Data.getItems()) {
            Top250FilmsModel top250FilmsModel = Top250FilmsModel.builder().idFilm(item.getId()).build();
            filmsRepository.save(top250FilmsModel);
        }
    }
}
