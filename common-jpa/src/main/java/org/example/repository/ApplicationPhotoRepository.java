package org.example.repository;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationPhotoRepository extends JpaRepository<ApplicationPhoto,Long> {
}
