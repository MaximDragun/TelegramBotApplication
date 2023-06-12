package org.example.service.strategyBotCommand.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.ApplicationUser;
import org.example.enums.BotCommands;
import org.example.service.strategyBotCommand.interfaces.BotCommandStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.springframework.stereotype.Service;

import static org.example.enums.BotCommands.*;

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
        return "🧾 Список доступных команд:\n"+
                WHAT_TO_SEE+"👀 - получить ссылку на случайный фильм или сериал с сайта Imdb топ 250\n"+
                CAT_PICTURE+"\uD83D\uDC31 - фото или гифка случайного котика\n"+
                REGISTRATION+"\uD83D\uDD13 - регистрация пользователя для использования файлообменника\n" +
                "В настоящее время регистрация через Gmail недоступна!\n"+
                "В настоящее время автоматическое скачивание файлов через веб-приложение телеграма в Google Chrome недоступно!\n"+
                RESEND+"📬 - отправить письмо на указанную почту еще раз\n"+
                CHOOSE_ANOTHER_EMAIL+"\uD83D\uDCE9 - изменить почту указанную при регистрации\n"+
                HELP+"\uD83C\uDD98 - посмотреть список доступных команд\n"+
                CANCEL+"❌ - отменяет выбор ввода почты при регистрации или сменe почты\n"+
                "Чтобы пользоваться файлообменником просто прикрепите к сообщению любой документ или фото и отправьте \uD83D\uDCF7\n"+
                "В настоящее время ограничение на размер загружаемого документа - 10мб";
    }
}
