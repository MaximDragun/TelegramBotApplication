package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.service.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.service.enums.BotCommands.HELP;

@RequiredArgsConstructor
@Service
public class BotCommandStrategyHelp implements BotCommandStrategy {
    private final SendMessageUtil sendMessageUtil;

    @Override
    public void sendAnswer(ApplicationUser applicationUser, long chatId) {
        sendMessageUtil.sendAnswerDefault(help(), chatId);
    }

    @Override
    public BotCommands myCommands() {
        return HELP;
    }

    private String help() {
        return """
                🧾 Список доступных команд:
                /what_to_see👀 - получить ссылку на случайный фильм или сериал с сайта Imdb топ 250
                /cat\uD83D\uDC31 - фото случайного котика
                /registration\uD83D\uDD13 - регистрация пользователя для использования файлообменника
                /resend_email📬 - отправить письмо на указанную почту еще раз
                /change_email\uD83D\uDCE9 - изменить почту указанную при регистрации
                /help\uD83C\uDD98 - посмотреть список доступных команд
                /cancel❌ - отменяет выбор ввода почты при регистрации или сменe почты
                """;
    }
}
