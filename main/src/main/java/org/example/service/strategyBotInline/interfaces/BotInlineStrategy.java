package org.example.service.strategyBotInline.interfaces;

import org.example.service.enums.BotInline;

public interface BotInlineStrategy {
    void sendAnswer(long chatId);
    BotInline myCommands();
}
