package org.example.service.strategyBotInline.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.FilmService;
import org.example.service.enums.BotInline;
import org.example.service.strategyBotInline.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BotInlineStrategySeries implements BotInlineStrategy {
    private final FilmService filmService;
    private final SendMessageUtil sendMessageUtil;
    @Override
    public void sendAnswer(long chatId) {
        sendMessageUtil.sendAnswerForFilmInlineWithLink(filmService.getLinkForSeries(), chatId);
    }

    @Override
    public BotInline myCommands() {
        return BotInline.SERIAL;
    }
}
