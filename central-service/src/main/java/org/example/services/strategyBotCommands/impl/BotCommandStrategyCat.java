package org.example.services.strategyBotCommands.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.BotCommands;
import org.example.models.ApplicationUser;
import org.example.services.strategyBotCommands.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.CAT_PICTURE;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyCat implements BotCommandStrategy {
    //    private final CatPictureService catPictureService;
//    private final SendPhotoUtil sendPhotoUtil;
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
//        sendPhotoUtil.sendAnswerForCatInline(catPictureService.getLinkCatPicture(), chatId);
        sendMessageUtil.sendAnswerDefault("В данный момент функция не работает!\n" +
                "Попробуйте позже!", chatId);
    }

    @Override
    public BotCommands myCommands() {
        return CAT_PICTURE;
    }
}
