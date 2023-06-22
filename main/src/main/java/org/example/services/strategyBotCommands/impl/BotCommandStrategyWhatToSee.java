package org.example.services.strategyBotCommands.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.BotCommands;
import org.example.models.ApplicationUser;
import org.example.services.strategyBotCommands.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.WHAT_TO_WATCH;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyWhatToSee implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerForFilmInline(getStartFilmText(), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return WHAT_TO_WATCH;
    }

    private String getStartFilmText() {
        return "Полнометражка или Сериал? \uD83D\uDD0E";
    }
}
