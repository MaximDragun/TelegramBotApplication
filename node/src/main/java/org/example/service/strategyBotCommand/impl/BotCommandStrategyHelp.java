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
                Список доступных команд:
                /registration - регистрация пользователя
                /resend_email - отправить письмо регистрации еще раз
                /choose_another_email - изменить почту для регистрации или после регистрации
                /cancel - отмена выполнения текущей команды
                """;
    }
}
