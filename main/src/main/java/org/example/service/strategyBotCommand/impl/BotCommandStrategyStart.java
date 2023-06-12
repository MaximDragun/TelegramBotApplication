package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.BotCommands;
import org.example.model.ApplicationUser;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyStart implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefault(start(), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return START;
    }

    private String start() {
        return " Приветствую, путник!👋\n" +
                "Данный бот умеет немного, основное его великое предназначение - быть файлообменником для документов\n" +
                "Так же он умеет советовать фильмы и сериалы из топа imdb - команда " + WHAT_TO_WATCH + " 👀\n" +
                "И конечно же умеет отправлять котиков, без них сейчас никак - команда " + CAT_PICTURE + " \uD83D\uDC31\n" +
                "Для использования файлообменника необходимо сначала зарегистрироваться, команда " + REGISTRATION + " \uD83D\uDD13\n" +
                "В настоящее время ограничение на размер загружаемого документа - 10мб\n"+
                "В настоящее время регистрация через Gmail недоступна!\n" +
                "В настоящее время автоматическое скачивание файлов через веб-приложение телеграма в Google Chrome недоступно!\n"+
                "P.S. можно использовать временную почту если не хочется палить свою! https://temp-mail.org/ru/\n" +
                "Чтобы посмотреть список доступных команд введите " + HELP + " \uD83C\uDD98\n" +
                "Все доступные команды есть в меню бота";

    }
}
