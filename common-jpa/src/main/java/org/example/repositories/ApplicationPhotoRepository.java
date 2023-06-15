package org.example.repositories;

import org.example.models.ApplicationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationPhotoRepository extends JpaRepository<ApplicationPhoto,Long> {
}
