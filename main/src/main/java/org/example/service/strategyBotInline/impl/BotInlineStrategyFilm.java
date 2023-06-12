package org.example.service.strategyBotInline.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.FilmService;
import org.example.service.enums.BotInline;
import org.example.service.strategyBotInline.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BotInlineStrategyFilm implements BotInlineStrategy {
    private final FilmService filmService;
    private final SendMessageUtil sendMessageUtil;
    @Override
    public void sendAnswer(long chatId) {
        sendMessageUtil.sendAnswerForFilmInline(filmService.getLinkForFilm(), chatId);
    }

    @Override
    public BotInline myCommands() {
        return BotInline.FILM;
    }
}
