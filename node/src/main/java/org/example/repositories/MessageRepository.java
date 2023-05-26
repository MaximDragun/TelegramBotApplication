package org.example.repositories;

import org.example.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
}
