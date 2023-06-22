package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.BotCommands;
import org.example.exceptions.UploadFileException;
import org.example.models.ApplicationUser;
import org.example.models.enums.UserState;
import org.example.services.enums.BotInline;
import org.example.services.interfaces.ApplicationUserService;
import org.example.services.interfaces.FileService;
import org.example.services.interfaces.MainService;
import org.example.services.strategyBotCommands.interfaces.BotCommandStrategy;
import org.example.services.strategyBotInlines.interfaces.BotInlineStrategy;
import org.example.util.interfaces.SendMessageUtil;
import org.example.services.interfaces.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.example.enums.BotCommands.CANCEL;
import static org.example.enums.BotCommands.HELP;
import static org.example.models.enums.UserState.BASIC_STATE;
import static org.example.models.enums.UserState.WAIT_FOR_EMAIL;

@RequiredArgsConstructor
@Slf4j
@Service
public class MainServiceImpl implements MainService {
    private final FileService fileService;
    private final ApplicationUserService applicationUserService;
    private final SendMessageUtil sendMessageUtil;
    private final TransactionalService transactionalService;
    private Map<BotCommands, BotCommandStrategy> strategyMapCommand;
    private Map<BotInline, BotInlineStrategy> strategyMapInline;

    @Autowired
    public void setStrategyMapCommand(List<BotCommandStrategy> botCommandStrategies, List<BotInlineStrategy> botInlineStrategies) {
        this.strategyMapCommand = botCommandStrategies.stream().collect(toMap(BotCommandStrategy::myCommands,
                identity(),
                (key1, key2) -> key1,
                () -> new EnumMap<>(BotCommands.class)));

        this.strategyMapInline = botInlineStrategies.stream().collect(toMap(BotInlineStrategy::myCommands,
                identity(),
                (key1, key2) -> key1,
                () -> new EnumMap<>(BotInline.class)));
    }

    @Override
    public void processDocumentMessage(Update update) {
        transactionalService.saveMessage(update);
        ApplicationUser applicationUser = transactionalService.findOrSaveApplicationUser(update);
        long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, applicationUser)) {
            return;
        }
        Long fileSize = update.getMessage().getDocument().getFileSize();
        if (((fileSize / 1024.0) / 1024.0) > 20) {
            String error = "Вы отправили документ больше установленных 20 мб❌\n" +
                    "Выберите файл подходящий под ограничение!";
            sendMessageUtil.sendAnswerDefault(error, chatId);
            return;
        }
        try {
            String link = fileService.processDoc(update.getMessage());
            String answer = "Документ успешно загружен ✅ \n"
                    + "Ссылка для скачивания: " + link;
            sendMessageUtil.sendAnswerForFormatLink(answer, chatId);
        } catch (UploadFileException ex) {
            log.error("Ошибка при загрузке документа из телеграма, ошибка -  {}", ex.getMessage());
            String error = "К сожалению, загрузка файла не удалась. ❌\n" +
                    "Повторите попытку позже.";
            sendMessageUtil.sendAnswerDefault(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        transactionalService.saveMessage(update);
        ApplicationUser applicationUser = transactionalService.findOrSaveApplicationUser(update);
        long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, applicationUser)) {
            return;
        }
        try {
            String link = fileService.processPhoto(update.getMessage());
            String answer = "Фото успешно загружено ✅ \n"
                    + "Ссылка для скачивания: " + link;
            sendMessageUtil.sendAnswerForFormatLink(answer, chatId);
        } catch (UploadFileException ex) {
            log.error("Ошибка загрузки файла", ex);
            String error = "К сожалению, загрузка файла не удалась. ❌\n" +
                    "Повторите попытку позже.";
            sendMessageUtil.sendAnswerDefault(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(long chatId, ApplicationUser applicationUser) {
        if (!applicationUser.getIsActive()) {
            String error = "Зарегистрируйтесь для отправки фото и документов! \ud83d\udee1";
            sendMessageUtil.sendAnswerDefault(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(applicationUser.getUserState())) {
            String error = "Отмените последнюю команду с помощью " + CANCEL + " для отправки файлов";
            sendMessageUtil.sendAnswerDefault(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processCallBack(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String text = update.getCallbackQuery().getData();
        BotInline serviceCommand = BotInline.fromValue(text);
        if (serviceCommand != null) {
            BotInlineStrategy botInlineStrategy = strategyMapInline.get(serviceCommand);
            botInlineStrategy.sendAnswer(chatId);
        } else {
            sendMessageUtil.sendAnswerDefault("Неизвестная команда! Чтобы посмотреть список доступных команд введите " + HELP, chatId);
        }
    }

    @Override
    public void processTextMessage(Update update) {
        transactionalService.saveMessage(update);
        ApplicationUser applicationUser = transactionalService.findOrSaveApplicationUser(update);
        UserState userState = applicationUser.getUserState();
        String text = update.getMessage().getText();
        String output;

        BotCommands serviceCommand = BotCommands.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = transactionalService.cancelProcess(applicationUser);
            sendMessageUtil.sendAnswerDefault(output, update.getMessage().getChatId());
        } else if (BASIC_STATE.equals(userState)) {
            processServiceCommand(applicationUser, text, update.getMessage().getChatId());
        } else if (WAIT_FOR_EMAIL.equals(userState)) {
            output = applicationUserService.setEmail(applicationUser, text);
            sendMessageUtil.sendAnswerDefault(output, update.getMessage().getChatId());
        } else {
            log.error("Ошибка при приеме команды, текущее состояние - {}", userState);
            output = "Неизвестное состояние введите " + CANCEL;
            sendMessageUtil.sendAnswerDefault(output, update.getMessage().getChatId());
        }
    }

    private void processServiceCommand(ApplicationUser applicationUser, String text, long chatId) {
        BotCommands serviceCommand = BotCommands.fromValue(text);
        if (serviceCommand != null) {
            BotCommandStrategy botCommandStrategy = strategyMapCommand.get(serviceCommand);
            botCommandStrategy.sendAnswer(applicationUser, chatId);
        } else {
            sendMessageUtil.sendAnswerDefault("Неизвестная команда! Чтобы посмотреть список доступных команд введите " + HELP, chatId);
        }
    }


}
