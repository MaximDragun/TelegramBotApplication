package org.example.service.strategyBotInline.interfaces;

import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.enums.BotInline;

public interface BotInlineStrategy {
    void sendAnswer(long chatId);
    BotInline myCommands();
}
