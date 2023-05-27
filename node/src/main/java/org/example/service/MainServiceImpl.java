package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ApplicationUser;
import org.example.model.MessageEntity;
import org.example.model.enums.UserState;
import org.example.repositories.MessageRepository;
import org.example.service.enums.BotCommands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.example.repository.ApplicationUserRepository;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.example.model.enums.UserState.*;
import static org.example.service.enums.BotCommands.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MessageRepository messageRepository;
    private final ProducerService producerService;
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public void processTextMessage(Update update) {
        saveMessage(update);


        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        UserState userState = applicationUser.getUserState();
        String text = update.getMessage().getText();
        String output = "";


        if (CANCEL.equals(text))
            output = cancelProcess(applicationUser);
        else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(applicationUser, text);
        } else if (WAIT_FOR_EMAIL.equals(userState)) {
            //TODO добавить обработку почты
        } else {
            log.error("Ошибка в приеме текста " + userState);
            output = "Неизвестное состояние введите /cancel";
        }
        sendAnswer(output, update.getMessage().getChatId());


    }

    @Override
    public void processDocumentMessage(Update update) {
        saveMessage(update);
        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, applicationUser)) {
            return;
        }
        //TODO: Добавить сохранение документа
        String answer = "Документ успешно загружен! Ссылка для скачивания ******";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowToSendContent(long chatId, ApplicationUser applicationUser) {
        UserState userState = applicationUser.getUserState();
        if (!applicationUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте свою учетную запись";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(applicationUser.getUserState())){
            String error = "Отмените последнюю команду с помощью /cancel для отправки файлов";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveMessage(update);
        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, applicationUser)) {
            return;
        }
        //TODO: Добавить сохранение фото
        String answer = "Фото успешно загружено! Ссылка для скачивания ******";
        sendAnswer(answer, chatId);
    }

    private void sendAnswer(String output, long chatId) {
//        ThreadLocalRandom tr = ThreadLocalRandom.current();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
//        sendMessage.setText("Сегодня: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy HH:mm", Locale.of("RU"))) + "\n" +
//                "За окном температура тепла: " + tr.nextInt(0, 10) + " градусов");
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(ApplicationUser applicationUser, String text) {
        if (REGISTRATION.equals(text)) {
            //TODO: добавить регистрацию
            return "Функицонал пока отстутсвует!";
        } else if (HELP.equals(text)) {
            return help();
        } else if (START.equals(text)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else
            return "Чтобы посмотреть список доступных команд введите /help";
    }

    private String help() {
        return """
                Список доступных команд:
                /cancel - отмена выполнения текущей команды
                /registration - регистрация пользователя
                """;
    }

    private String cancelProcess(ApplicationUser applicationUser) {
        applicationUser.setUserState(BASIC_STATE);
        applicationUserRepository.save(applicationUser);
        return "Команда отменена";


    }

    private ApplicationUser findOrSaveApplicationUser(Update update) {
        User user = update.getMessage().getFrom();
        Optional<ApplicationUser> persUser = applicationUserRepository.findApplicationUserByTelegramUserId(user.getId());
        if (persUser.isPresent())
            return persUser.get();
        else {
            ApplicationUser transientUser = ApplicationUser.builder()
                    .telegramUserId(user.getId())
                    .userName(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    //TODO
                    .isActive(true)
                    .userState(BASIC_STATE)
                    .build();
            return applicationUserRepository.save(transientUser);
        }
    }

    private void saveMessage(Update update) {
        MessageEntity build = MessageEntity.builder()
                .update(update)
                .build();
        messageRepository.save(build);
    }
}
