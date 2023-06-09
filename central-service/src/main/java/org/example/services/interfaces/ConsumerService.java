package org.example.services.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;


public interface ConsumerService {

    void consumeTextMessageUpdate(Update update);

    void consumePhotoMessageUpdate(Update update);

    void consumeDocumentMessageUpdate(Update update);
    void consumeCallBackUpdate(Update update);

}
