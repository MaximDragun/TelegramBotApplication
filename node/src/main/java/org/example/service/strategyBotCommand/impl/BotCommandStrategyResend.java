package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.ApplicationUserService;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyResend implements BotCommandStrategy {
    private final ApplicationUserService applicationUserService;

    @Override
    public String sendAnswer(ApplicationUser applicationUser) {
       return applicationUserService.resendEmail(applicationUser);
    }

    @Override
    public BotCommands myCommands() {
        return BotCommands.RESEND;
    }
}
