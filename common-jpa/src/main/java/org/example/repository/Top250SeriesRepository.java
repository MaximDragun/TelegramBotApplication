package org.example.repository;

import org.example.model.Top250SeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Top250SeriesRepository extends JpaRepository<Top250SeriesModel, Integer> {
}