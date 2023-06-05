package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import static org.example.model.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produceAnswerMessage(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }

    @Override
    public void produceAnswerPhoto(SendPhoto sendPhoto) {
        rabbitTemplate.convertAndSend(ANSWER_PHOTO, sendPhoto);
    }

    @Override
    public void produceAnswerAnimation(SendAnimation sendAnimation) {
        rabbitTemplate.convertAndSend(ANSWER_ANIMATION, sendAnimation);
    }
}
