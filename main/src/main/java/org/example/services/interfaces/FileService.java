package org.example.services.interfaces;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    String processDoc(Message telegramMessage);
    String processPhoto(Message telegramMessage);
}