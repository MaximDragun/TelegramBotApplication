package org.example.services.interfaces;

import org.example.models.ApplicationDocument;
import org.example.models.ApplicationPhoto;
import org.example.models.BinaryContent;
import org.example.services.enums.LinkType;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message telegramMessage);
    ApplicationPhoto processPhoto(Message telegramMessage);
    String genericLink(Long fileId, LinkType linkType);
    BinaryContent getPersistentBinaryContent(ResponseEntity<String> response);
}