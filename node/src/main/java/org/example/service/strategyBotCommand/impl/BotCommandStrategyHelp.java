package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.example.util.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyHelp implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;
    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefoult(help(),chatId);
    }

    @Override
    public BotCommands myCommands() {
        return HELP;
    }

    private String help() {
        return """
                🧾 Список доступных команд:
                /registration - регистрация пользователя для использования файлообменника
                /resend_email - отправить письмо на указанную почту еще раз
                /choose_another_email - изменить почту указанную при регистрации
                /what_to_see - получить ссылку на случайный фильм или сериал с сайта Imdb топ 250
                /help - посмотреть список доступных команд
                /cancel - отмена выполнения текущей команды
                """;
    }
}
