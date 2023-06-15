package org.example.repositories;

import org.example.models.Top250SeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Top250SeriesRepository extends JpaRepository<Top250SeriesModel, Integer> {
}