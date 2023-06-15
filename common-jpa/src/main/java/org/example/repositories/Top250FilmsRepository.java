package org.example.repositories;

import org.example.models.Top250FilmsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Top250FilmsRepository extends JpaRepository<Top250FilmsModel, Integer> {
}