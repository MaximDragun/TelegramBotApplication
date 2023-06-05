package org.example.service.strategyBotCommand.interfaces;

import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;

public interface BotCommandStrategy {
    void sendAnswer(ApplicationUser applicationUser, long chatId);
    BotCommands myCommands();
}