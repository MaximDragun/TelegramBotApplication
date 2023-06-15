package org.example.services.strategyBotCommands.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.ApplicationUser;
import org.example.services.interfaces.ApplicationUserService;
import org.example.enums.BotCommands;
import org.example.services.strategyBotCommands.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyResend implements BotCommandStrategy {
    private final ApplicationUserService applicationUserService;
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefault(applicationUserService.resendEmail(applicationUser), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return RESEND;
    }
}
