package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.interfaces.ApplicationUserService;
import org.example.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

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
        return BotCommands.RESEND;
    }
}
