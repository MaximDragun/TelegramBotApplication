package org.example.services.strategyBotCommands.interfaces;

import org.example.models.ApplicationUser;
import org.example.enums.BotCommands;

public interface BotCommandStrategy {
    void sendAnswer(ApplicationUser applicationUser, long chatId);
    BotCommands myCommands();
}
