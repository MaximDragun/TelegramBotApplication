package org.example.util.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.services.interfaces.ProducerService;
import org.example.util.interfaces.SendMessageUtil;
import org.example.util.interfaces.SendPhotoUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


@Slf4j
@RequiredArgsConstructor
@Component
public class SendPhotoUtilImpl implements SendPhotoUtil {
    private final SendMessageUtil sendMessageUtil;
    private final ProducerService producerService;
    private final InlineKeyboardMarkup inlineKeyboardMarkupForCat;

    @Override
    public void sendAnswerForCatInline(String url, long chatId) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(url));
            sendPhoto.setReplyMarkup(inlineKeyboardMarkupForCat);
            sendPhoto.setDisableNotification(true);
            producerService.produceAnswerPhoto(sendPhoto);
        } catch (Exception e) {
            log.error("Ошибка при отправке фото с URL {}", url, e);
            sendMessageUtil.sendAnswerDefault(e.getMessage(), chatId);
        }
    }
}
