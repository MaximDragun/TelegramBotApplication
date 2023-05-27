package org.example.repository;

import org.example.model.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent,Long> {
}
