package org.example.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface AnswerConsumer {
    void consumerMessage(SendMessage sendMessage);
    void consumerPhoto(SendPhoto sendPhoto);
     void consumerAnimation(SendAnimation sendAnimation);
}
