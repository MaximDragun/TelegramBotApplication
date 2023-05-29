package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.UploadFileException;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.ApplicationUser;
import org.example.model.MessageEntity;
import org.example.model.enums.UserState;
import org.example.repository.ApplicationUserRepository;
import org.example.repository.MessageRepository;
import org.example.service.ApplicationUserService;
import org.example.service.FileService;
import org.example.service.MainService;
import org.example.service.ProducerService;
import org.example.service.enums.BotCommands;
import org.example.service.enums.LinkType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static org.example.model.enums.UserState.BASIC_STATE;
import static org.example.model.enums.UserState.WAIT_FOR_EMAIL;
import static org.example.service.enums.BotCommands.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MessageRepository messageRepository;
    private final ProducerService producerService;
    private final ApplicationUserRepository applicationUserRepository;
    private final FileService fileService;
    private final ApplicationUserService applicationUserService;

    @Override
    public void processTextMessage(Update update) {
        saveMessage(update);


        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        UserState userState = applicationUser.getUserState();
        String text = update.getMessage().getText();
        String output = "";

        BotCommands serviceCommand = BotCommands.fromValue(text);
        if (CANCEL.equals(serviceCommand))
            output = cancelProcess(applicationUser);
        else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(applicationUser, text);
        } else if (WAIT_FOR_EMAIL.equals(userState)) {
            output = applicationUserService.setEmail(applicationUser, text);
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
        try {
            ApplicationDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.genericLink(doc.getId(), LinkType.GET_DOC);
            String answer = "Документ успешно загружен! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex.getMessage(), ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(long chatId, ApplicationUser applicationUser) {
        UserState userState = applicationUser.getUserState();
        if (!applicationUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте свою учетную запись";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(applicationUser.getUserState())) {
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

        try {
            ApplicationPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.genericLink(photo.getId(), LinkType.GET_PHOTO);
            String answer = "Фото успешно загружено! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex.getMessage(), ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
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
        var serviceCommand = BotCommands.fromValue(text);
        if (REGISTRATION.equals(serviceCommand)) {
            return applicationUserService.registerUser(applicationUser);
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
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
        Optional<ApplicationUser> persUser = applicationUserRepository.findByTelegramUserId(user.getId());
        if (persUser.isPresent())
            return persUser.get();
        else {
            ApplicationUser transientUser = ApplicationUser.builder()
                    .telegramUserId(user.getId())
                    .userName(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .isActive(false)
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
