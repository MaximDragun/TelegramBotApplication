package org.example.services.strategyBotInlines.impl;

import lombok.RequiredArgsConstructor;
import org.example.services.interfaces.CatPictureService;
import org.example.services.enums.BotInline;
import org.example.services.strategyBotInlines.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendPhotoUtil;
import org.springframework.stereotype.Service;

import static org.example.services.enums.BotInline.*;
@RequiredArgsConstructor
@Service
public class BotInlineStrategyCat implements BotInlineStrategy {
    private final CatPictureService catPictureService;
    private final SendPhotoUtil sendPhotoUtil;
    @Override
    public void sendAnswer(long chatId) {
        sendPhotoUtil.sendAnswerForCatInline(catPictureService.getLinkCatPicture(),chatId);
    }

    @Override
    public BotInline myCommands() {
        return CAT_PICTURE;
    }
}
