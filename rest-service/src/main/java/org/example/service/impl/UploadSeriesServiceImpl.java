package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.Top250Data;
import org.example.dto.Top250DataDetail;
import org.example.model.Top250SeriesModel;
import org.example.repository.Top250SeriesRepository;
import org.example.service.interfaces.UploadSeriesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UploadSeriesServiceImpl implements UploadSeriesService {
    private final Top250SeriesRepository seriesRepository;
    private final RestTemplate restTemplate;
    @Value("${imdb.series.uri}")
    private String seriesUri;
    @Value("${imdb-api.token}")
    private String imdbApiToken;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void uploadSeries() {
        if (seriesRepository.findById(1).isPresent()) return;
        Top250Data top250Data = restTemplate.getForObject(seriesUri, Top250Data.class, imdbApiToken);
        if (top250Data == null)
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Не удалось получить список сериалов от imdb ip");
        if (!top250Data.getErrorMessage().isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, top250Data.getErrorMessage());
        for (Top250DataDetail item : top250Data.getItems()) {
            Top250SeriesModel top250SeriesModel = Top250SeriesModel.builder().idSeries(item.getId()).build();
            seriesRepository.save(top250SeriesModel);
        }
    }
}
