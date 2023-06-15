package org.example.repositories;

import org.example.models.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent,Long> {
}
