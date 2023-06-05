package org.example.util.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.ProducerService;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@RequiredArgsConstructor
@Component
public class SendMessageUtilImpl implements SendMessageUtil {
    private final ProducerService producerService;
    private final InlineKeyboardMarkup inlineKeyboardMarkupForFilm;

    @Override
    public void sendAnswerDefault(String output, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setDisableNotification(true);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendAnswerForFormatLink(String output, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setDisableNotification(true);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendStartKeyBoard(String output, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setDisableNotification(true);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendAnswerForFilmInline(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Полнометражка или Сериал? \uD83D\uDD0E");
        sendMessage.setReplyMarkup(inlineKeyboardMarkupForFilm);
        sendMessage.setDisableNotification(true);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendAnswerForFilmInlineWithLink(String output, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setReplyMarkup(inlineKeyboardMarkupForFilm);
        sendMessage.setDisableNotification(true);
        producerService.produceAnswerMessage(sendMessage);
    }

}
