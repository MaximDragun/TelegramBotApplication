package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.START;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyStart implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendStartKeyBoard(start(), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return START;
    }

    private String start() {
        return """
                Приветствую путник!👋
                Данный бот умеет немного, основное его великое предназначение - быть файлообменником фотографий и документов.
                Так же он умеет советовать фильмы и сериалы из топа imdb - команда /what_to_see 👀
                И конечно же умеет отправлять котиков, без них сейчас никак - команда /cat \uD83D\uDC31
                Для использования файлообменника необходимо сначала зарегистрироваться, команда /registration \uD83D\uDD13.
                P.S. можно использовать temp-mail если не хочется палить свою почту! :)
                Чтобы посмотреть список доступных команд введите /help \uD83C\uDD98
                Все доступные команды есть в меню бота
                """;
    }
}
