package org.example.service.strategyBotInline.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.enums.BotInline;
import org.example.service.interfaces.CatPictureService;
import org.example.service.strategyBotInline.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendAnimationUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotInline.CAT_GIF;

@RequiredArgsConstructor
@Service
public class BotInlineStrategyCatGif implements BotInlineStrategy {
    private final SendAnimationUtil sendAnimationUtil;
    private final CatPictureService catPictureService;

    @Override
    public void sendAnswer(long chatId) {
        sendAnimationUtil.sendAnswerForCatInline(catPictureService.getLinkCatGif(),chatId);
    }

    @Override
    public BotInline myCommands() {
        return CAT_GIF;
    }
}
