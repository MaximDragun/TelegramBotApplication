package org.example.service.interfaces;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.service.enums.LinkType;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message telegramMessage);
    ApplicationPhoto processPhoto(Message telegramMessage);
    String genericLink(Long fileId, LinkType linkType);
    BinaryContent getPersistentBinaryContent(ResponseEntity<String> response);
}