package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.interfaces.ApplicationUserService;
import org.example.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.CHOOSE_ANOTHER_EMAIL;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyChooseAnotherMail implements BotCommandStrategy {
    private final ApplicationUserService applicationUserService;
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefault(applicationUserService.chooseAnotherEmail(applicationUser), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return CHOOSE_ANOTHER_EMAIL;
    }
}
