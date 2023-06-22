package org.example.util.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.services.interfaces.ProducerService;
import org.example.util.interfaces.SendAnimationUtil;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendAnimationUtilImpl implements SendAnimationUtil {
    private final SendMessageUtil sendMessageUtil;
    private final ProducerService producerService;
    private final InlineKeyboardMarkup inlineKeyboardMarkupForCat;

    @Override
    public void sendAnswerForCatInline(String url, long chatId) {
        try {
            SendAnimation sendAnimation = new SendAnimation();
            sendAnimation.setChatId(chatId);
            sendAnimation.setAnimation(new InputFile(url));
            sendAnimation.setReplyMarkup(inlineKeyboardMarkupForCat);
            sendAnimation.setDisableNotification(true);
            producerService.produceAnswerAnimation(sendAnimation);
        } catch (Exception e) {
            log.error("Ошибка при отправке гифки с URL {}", url, e);
            sendMessageUtil.sendAnswerDefault(e.getMessage(), chatId);
        }
    }
}
