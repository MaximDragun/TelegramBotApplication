package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.TelegramBotMainService;
import org.example.service.interfaces.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import static org.example.model.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {
    private final TelegramBotMainService telegramBotMainService;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consumerMessage(SendMessage sendMessage) {
        telegramBotMainService.setView(sendMessage);
    }
    @Override
    @RabbitListener(queues = ANSWER_PHOTO)
    public void consumerPhoto(SendPhoto sendPhoto) {
        telegramBotMainService.setPhoto(sendPhoto);
    }
    @Override
    @RabbitListener(queues = ANSWER_ANIMATION)
    public void consumerAnimation(SendAnimation sendAnimation) {
        telegramBotMainService.setAnimation(sendAnimation);
    }
}
