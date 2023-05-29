package org.example.service;

import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message telegramMessage);
    ApplicationPhoto processPhoto(Message telegramMessage);
    String genericLink(Long fileId, LinkType linkType);
}