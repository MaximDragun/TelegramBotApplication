package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.example.enums.BotCommands.*;

@Slf4j
@Service
public class TelegramBot extends TelegramWebhookBot {
    @Value("${bot.name}")
    private String userName;
    @Value("${bot.uri}")
    private String botUri;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @PostConstruct
    public void init() {
        try {
            SetWebhook webhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            setWebhook(webhook);
            createBotMenu();
        } catch (TelegramApiException e) {
            log.error("Ошибка при инициализации бота", e);
        }
    }

    private void createBotMenu() throws TelegramApiException {
        List<BotCommand> menuBotCommands = new ArrayList<>();
        menuBotCommands.add(new BotCommand(WHAT_TO_WATCH.toString(), "Случайный фильм или сериал"));
        menuBotCommands.add(new BotCommand(CAT_PICTURE.toString(), "Фото и гифки котиков"));
        menuBotCommands.add(new BotCommand(REGISTRATION.toString(), "Регистрация пользователя"));
        menuBotCommands.add(new BotCommand(RESEND.toString(), "Отправить письмо еще раз"));
        menuBotCommands.add(new BotCommand(CHOOSE_ANOTHER_EMAIL.toString(), "Поменять указанную почту"));
        menuBotCommands.add(new BotCommand(HELP.toString(), "Список всех команд"));
        menuBotCommands.add(new BotCommand(CANCEL.toString(), "Отмена команды"));
        execute(new SetMyCommands(menuBotCommands, new BotCommandScopeDefault(), null));
    }

    public void sendAnswerMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке сообщения пользователю в чат {}", sendMessage.getChatId(), e);
            }
        }
    }

    public void sendAnswerPhoto(SendPhoto sendPhoto) {
        if (sendPhoto != null) {
            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке фото пользователю в чат {}", sendPhoto.getChatId(), e);
            }
        }
    }

    public void sendAnswerAnimation(SendAnimation sendAnimation) {
        if (sendAnimation != null) {
            try {
                execute(sendAnimation);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке гифки пользователю в чат {}", sendAnimation.getChatId(), e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }
}
