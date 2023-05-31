package org.example.service.strategyBotCommand;

import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;

public interface BotCommandStrategy {
    String sendAnswer(ApplicationUser applicationUser);
    BotCommands myCommands();
}
