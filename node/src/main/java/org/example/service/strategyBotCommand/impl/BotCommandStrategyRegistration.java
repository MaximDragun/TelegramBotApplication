package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.ApplicationUserService;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyRegistration implements BotCommandStrategy {
    private final ApplicationUserService applicationUserService;
    @Override
    public String sendAnswer(ApplicationUser applicationUser) {
        return applicationUserService.registerUser(applicationUser);
    }

    @Override
    public BotCommands myCommands() {
        return REGISTRATION;
    }
}
