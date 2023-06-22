package org.example.services.strategyBotInlines.impl;

import lombok.RequiredArgsConstructor;
import org.example.services.enums.BotInline;
import org.example.services.interfaces.CatPictureService;
import org.example.services.strategyBotInlines.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendAnimationUtil;
import org.springframework.stereotype.Service;

import static org.example.services.enums.BotInline.CAT_GIF;

@RequiredArgsConstructor
@Service
public class BotInlineStrategyCatGif implements BotInlineStrategy {
    private final SendAnimationUtil sendAnimationUtil;
    private final CatPictureService catPictureService;

    @Override
    public void sendAnswer(long chatId) {
        sendAnimationUtil.sendAnswerForCatInline(catPictureService.getLinkCatGif(), chatId);
    }

    @Override
    public BotInline myCommands() {
        return CAT_GIF;
    }
}
