package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyStart implements BotCommandStrategy {
    @Override
    public String sendAnswer(ApplicationUser applicationUser) {
        return start();
    }

    @Override
    public BotCommands myCommands() {
        return START;
    }

    private String start() {
        return """
                Приветствую путник!
                Данный бот умеет немного, основное его великое предназначение - быть файлообменником фотографий и документов.
                Для использования файлообменника необходимо сначала зарегистрироваться, команда /registration.
                P.S. можно использовать temp-mail если не хочется палить свою почту! :)
                Чтобы посмотреть список доступных команд введите /help
                """;
    }
}
