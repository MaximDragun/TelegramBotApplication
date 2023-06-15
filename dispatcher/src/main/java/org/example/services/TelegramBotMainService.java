package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.services.interfaces.UpdateProducer;
import org.example.util.MessageUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.example.models.RabbitQueue.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramBotMainService {
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private final TelegramBot telegramBot;

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Update is null");
            return;
        }
        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        } else {
            log.error("Unsupported message type {}", update);
        }
    }

    private void distributeMessagesByType(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocumentMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageType(update);
        }
    }

    private void setUnsupportedMessageType(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessage(update,
                "Тип сообщения не поддерживается");
        setView(sendMessage);
    }

    private void SetFileIsReceivedView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessage(update,
                "Файл получен! Идет обработка ⌛");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setPhoto(SendPhoto sendPhoto) {
        telegramBot.sendAnswerPhoto(sendPhoto);
    }

    public void setAnimation(SendAnimation sendAnimation) {
        telegramBot.sendAnswerAnimation(sendAnimation);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        SetFileIsReceivedView(update);
    }

    private void processCallbackQuery(Update update) {
        updateProducer.produce(CALLBACK_QUERY_UPDATE, update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        SetFileIsReceivedView(update);
    }
}