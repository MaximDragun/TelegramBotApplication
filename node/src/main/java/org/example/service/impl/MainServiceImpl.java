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
import org.example.service.FilmService;
import org.example.service.MainService;
import org.example.service.enums.BotCommands;
import org.example.service.enums.Films;
import org.example.service.enums.LinkType;
import org.example.service.strategyBotCommand.BotCommandStrategy;
import org.example.util.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.example.model.enums.UserState.BASIC_STATE;
import static org.example.model.enums.UserState.WAIT_FOR_EMAIL;
import static org.example.service.enums.BotCommands.CANCEL;

@RequiredArgsConstructor
@Slf4j
@Service
public class MainServiceImpl implements MainService {
    private final MessageRepository messageRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final FileService fileService;
    private final ApplicationUserService applicationUserService;
    private final FilmService filmService;
    private final SendMessageUtil sendMessageUtil;
    private Map<BotCommands, BotCommandStrategy> strategyMap;

    @Autowired
    public void setStrategyMap(List<BotCommandStrategy> botCommandStrategies) {
        this.strategyMap = botCommandStrategies.stream().collect(toMap(BotCommandStrategy::myCommands,
                identity(),
                (key1, key2) -> key1,
                () -> new EnumMap<>(BotCommands.class)));
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
            String answer = "Документ успешно загружен! ✅ \n"
                    + "Ссылка для скачивания: " + link;
            sendMessageUtil.sendAnswerForFormatLink(answer, chatId);
        } catch (UploadFileException ex) {
            log.error("Ошибка при загрузке документа из телеграма, ошибка -  {}", ex.getMessage());
            String error = "К сожалению, загрузка файла не удалась. ❌\n" +
                    "Повторите попытку позже.";
            sendMessageUtil.sendAnswerDefoult(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(long chatId, ApplicationUser applicationUser) {
        if (!applicationUser.getIsActive()) {
            String error = "Зарегистрируйтесь для отправки фото и документов! \ud83d\udee1";
            sendMessageUtil.sendAnswerDefoult(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(applicationUser.getUserState())) {
            String error = "Отмените последнюю команду с помощью /cancel для отправки файлов";
            sendMessageUtil.sendAnswerDefoult(error, chatId);
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
            String answer = "Фото успешно загружено! ✅ \n"
                    + "Ссылка для скачивания: " + link;
            sendMessageUtil.sendAnswerForFormatLink(answer, chatId);
        } catch (UploadFileException ex) {
            log.error("Ошибка загрузки файла", ex);
            String error = "К сожалению, загрузка файла не удалась. ❌\n" +
                    "Повторите попытку позже.";
            sendMessageUtil.sendAnswerDefoult(error, chatId);
        }
    }

    @Override
    public void processCallBack(Update update) {
        String link;
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String text = update.getCallbackQuery().getData();
        Films serviceCommand = Films.fromValue(text);
        if (serviceCommand == null) {
            log.error("Неизвестная команда CallBack от пользователя с id {} dataCallBack {}", update.getCallbackQuery().getFrom().getId(), text);
            link = "Выбрана неизвестная команда, попробуйте снова";
        } else link = serviceCommand.equals(Films.FILM) ? filmService.getLinkForFilm() : filmService.getLinkForSeries();
        sendMessageUtil.sendAnswerForFormatLinkFilmAddKeyBoard(link, chatId);
    }

    @Override
    public void processTextMessage(Update update) {
        saveMessage(update);
        ApplicationUser applicationUser = findOrSaveApplicationUser(update);
        UserState userState = applicationUser.getUserState();
        String text = update.getMessage().getText();
        String output;

        BotCommands serviceCommand = BotCommands.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(applicationUser);
            sendMessageUtil.sendAnswerDefoult(output, update.getMessage().getChatId());
        } else if (BASIC_STATE.equals(userState)) {
            processServiceCommand(applicationUser, text, update.getMessage().getChatId());
        } else if (WAIT_FOR_EMAIL.equals(userState)) {
            output = applicationUserService.setEmail(applicationUser, text);
            sendMessageUtil.sendAnswerDefoult(output, update.getMessage().getChatId());
        } else {
            log.error("Ошибка при приеме команды, текущее состояние - {}", userState);
            output = "Неизвестное состояние введите /cancel";
            sendMessageUtil.sendAnswerDefoult(output, update.getMessage().getChatId());
        }


    }

    private void processServiceCommand(ApplicationUser applicationUser, String text, long chatId) {
        BotCommands serviceCommand = BotCommands.fromValue(text);
        if (serviceCommand != null) {
            BotCommandStrategy botCommandStrategy = strategyMap.get(serviceCommand);
            botCommandStrategy.sendAnswer(applicationUser, chatId);
        } else {
            sendMessageUtil.sendAnswerDefoult("Неизвестная команда! Чтобы посмотреть список доступных команд введите /help", chatId);
        }
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
                    .chatId(String.valueOf(update.getMessage().getChatId()))
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
