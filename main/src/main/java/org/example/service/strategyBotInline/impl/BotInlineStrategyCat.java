package org.example.service.strategyBotInline.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.CatPictureService;
import org.example.service.enums.BotInline;
import org.example.service.strategyBotInline.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendPhotoUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotInline.*;
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
