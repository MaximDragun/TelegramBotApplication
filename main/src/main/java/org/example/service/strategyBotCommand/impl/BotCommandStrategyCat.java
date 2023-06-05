package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.interfaces.CatPictureService;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendPhotoUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.CAT_PICTURE;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyCat implements BotCommandStrategy {
    private final CatPictureService catPictureService;
    private final SendPhotoUtil sendPhotoUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendPhotoUtil.sendAnswerForCatInline(catPictureService.getLinkCatPicture(), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return CAT_PICTURE;
    }
}
