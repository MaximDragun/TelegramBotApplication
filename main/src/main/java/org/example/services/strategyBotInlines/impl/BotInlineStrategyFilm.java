package org.example.services.strategyBotInlines.impl;

import lombok.RequiredArgsConstructor;
import org.example.services.interfaces.FilmService;
import org.example.services.enums.BotInline;
import org.example.services.strategyBotInlines.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.services.enums.BotInline.*;

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
        return FILM;
    }
}
