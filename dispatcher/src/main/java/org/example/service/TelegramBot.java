package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TelegramBot extends TelegramWebhookBot {
    @Value("${bot.name}")
    private String userName;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.uri}")
    private String uri;

    @PostConstruct
    public void init() {
        try {
            SetWebhook buildWebhook = SetWebhook.builder()
                    .url(uri)
                    .build();
            setWebhook(buildWebhook);
            createBotMenu();
        } catch (TelegramApiException e) {
            log.error("Ошибка при инициализации бота", e);
        }
    }

    private void createBotMenu() throws TelegramApiException {
        List<BotCommand> menuBotCommands = new ArrayList<>();
        menuBotCommands.add(new BotCommand("/registration", "регистрация пользователя"));
        menuBotCommands.add(new BotCommand("/resend_email", "отправить письмо еще раз"));
        menuBotCommands.add(new BotCommand("/choose_another_email", "поменять указанную почту"));
        menuBotCommands.add(new BotCommand("/help", "список всех команд"));
        menuBotCommands.add(new BotCommand("/cancel", "отмена последней команды"));
        execute(new SetMyCommands(menuBotCommands, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotPath() {
        return "/search";
    }

    public void sendAnswerMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке сообщения пользователю", e);
            }
        }

    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }
    public void dfdf(){
        SendMessage df = new SendMessage();




    }

}
