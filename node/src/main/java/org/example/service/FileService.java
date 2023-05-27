package org.example.service;

import org.example.model.ApplicationDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    ApplicationDocument processDoc(Message externalMessage);
}