package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.FilmService;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.example.util.SendMessageUtil;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class BotCommandStrategyFilm implements BotCommandStrategy {
    private final FilmService filmService;
    private final SendMessageUtil sendMessageUtil;
    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId)  {
        sendMessageUtil.sendAnswerForFormatLinkFilmAddKeyBoard(filmService.getLinkForFilm(),chatId);
    }

    @Override
    public BotCommands myCommands() {
        return BotCommands.FILM;
    }
}
