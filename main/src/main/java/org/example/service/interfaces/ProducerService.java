package org.example.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface ProducerService {
    void produceAnswerMessage(SendMessage sendMessage);
    void produceAnswerPhoto(SendPhoto sendPhoto);
    void produceAnswerAnimation(SendAnimation sendAnimation);
}
