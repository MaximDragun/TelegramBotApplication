package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.interfaces.ApplicationUserService;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.REGISTRATION;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyRegistration implements BotCommandStrategy {
    private final ApplicationUserService applicationUserService;
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefault(applicationUserService.registerUser(applicationUser), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return REGISTRATION;
    }
}
