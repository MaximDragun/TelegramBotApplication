package org.example.services.strategyBotCommands.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.BotCommands;
import org.example.models.ApplicationUser;
import org.example.services.interfaces.ApplicationUserService;
import org.example.services.strategyBotCommands.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.REGISTRATION;

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
