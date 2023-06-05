package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.WHAT_TO_SEE;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyWhatToSee implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerForFilmInline(chatId);
    }

    @Override
    public BotCommands myCommands() {
        return WHAT_TO_SEE;
    }
}
