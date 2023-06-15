package org.example.services.strategyBotInlines.interfaces;

import org.example.services.enums.BotInline;

public interface BotInlineStrategy {
    void sendAnswer(long chatId);
    BotInline myCommands();
}
