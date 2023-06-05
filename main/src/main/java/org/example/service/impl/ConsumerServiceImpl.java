package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.ConsumerService;
import org.example.service.interfaces.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.example.model.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
        mainService.processTextMessage(update);
    }

    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    @Override
    public void consumePhotoMessageUpdate(Update update) {
        mainService.processPhotoMessage(update);
    }

    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    @Override
    public void consumeDocumentMessageUpdate(Update update) {
        mainService.processDocumentMessage(update);
    }

    @RabbitListener(queues = CALLBACK_QUERY_UPDATE)
    @Override
    public void consumeCallBackUpdate(Update update) {
        mainService.processCallBack(update);
    }
}
