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
        SendMessage sendMessage = getFormedMessage(output, chatId);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendAnswerForFormatLink(String output, long chatId) {
        SendMessage sendMessage = getFormedMessage(output, chatId);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        producerService.produceAnswerMessage(sendMessage);
    }

    @Override
    public void sendAnswerForFilmInline(String output, long chatId) {
        SendMessage sendMessage = getFormedMessage(output, chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkupForFilm);
        producerService.produceAnswerMessage(sendMessage);
    }
    
    private SendMessage getFormedMessage(String output, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setDisableNotification(true);
        return sendMessage;
    }

}
