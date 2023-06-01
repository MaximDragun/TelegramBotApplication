package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.*;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyHelp implements BotCommandStrategy {
    @Override
    public String sendAnswer(ApplicationUser applicationUser) {
        return help();
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
                /help - посмотреть список доступных команд
                /cancel - отмена выполнения текущей команды
                """;
    }
}
