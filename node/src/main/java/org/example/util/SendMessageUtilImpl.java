package org.example.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.service.ProducerService;
import org.example.service.enums.Films;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Component
public class SendMessageUtilImpl implements SendMessageUtil {
    private final ProducerService producerService;
    private SendMessage sendMessage;
    @Override
    public void sendAnswerDefoult(String output, long chatId) {
        //        ThreadLocalRandom tr = ThreadLocalRandom.current();
        sendMessage.setChatId(chatId);
//        sendMessage.setText("Сегодня: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy HH:mm", Locale.of("RU"))) + "\n" +
//                "За окном температура тепла: " + tr.nextInt(0, 10) + " градусов");
        sendMessage.setText(output);
        addDefaultKeyBoard(sendMessage);
        producerService.produceAnswer(sendMessage);
    }

    @Override
    public void sendAnswerForFormatLink(String output, long chatId) {
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        addDefaultKeyBoard(sendMessage);
        producerService.produceAnswer(sendMessage);
    }

    @Override
    public void sendAnswerForFilmInline(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("Полнометражка или Сериал? \uD83D\uDD0E");

        InlineKeyboardMarkup inline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listsInlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> listInlineButtons = new ArrayList<>();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("Полнометражка \uD83C\uDFAC")
                .callbackData(Films.FILM.toString())
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("Сериал \uD83C\uDF7F")
                .callbackData(Films.SERIAL.toString())
                .build();
        listInlineButtons.add(button1);
        listInlineButtons.add(button2);
        listsInlineButtons.add(listInlineButtons);
        inline.setKeyboard(listsInlineButtons);
        sendMessage.setReplyMarkup(inline);
        producerService.produceAnswer(sendMessage);
    }

    @Override
    public void sendAnswerForFormatLinkFilmAddKeyBoard(String output, long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Полнометражка \uD83C\uDFAC");
        keyboardRow.add("Сериал \uD83C\uDF7F");
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        producerService.produceAnswer(sendMessage);
    }
    private void addDefaultKeyBoard(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Первый \uD83C\uDFAC");
        keyboardRow.add("Второй \uD83C\uDF7F");
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        //TODO:дописать все команды в клавиатуру
    }
}
