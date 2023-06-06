package org.example.configuration;

import org.example.EncryptionString;
import org.example.EncryptionTool;
import org.example.service.enums.BotInline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MainConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public EncryptionTool getEncryptTool() {
        return new EncryptionTool(salt);
    }
    @Bean
    public EncryptionString getEncryptionString() {
        return new EncryptionString(salt);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public InlineKeyboardMarkup inlineKeyboardMarkupForFilm() {
        InlineKeyboardMarkup inline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listsInlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> listInlineButtons = new ArrayList<>();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("Полнометражка \uD83C\uDFAC")
                .callbackData(BotInline.FILM.toString())
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("Сериал \uD83C\uDF7F")
                .callbackData(BotInline.SERIAL.toString())
                .build();
        listInlineButtons.add(button1);
        listInlineButtons.add(button2);
        listsInlineButtons.add(listInlineButtons);
        inline.setKeyboard(listsInlineButtons);
        return inline;
    }

    @Bean
    public InlineKeyboardMarkup inlineKeyboardMarkupForCat() {
        InlineKeyboardMarkup inline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listsInlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> listInlineButtons = new ArrayList<>();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("Пикча \uD83D\uDC31")
                .callbackData(BotInline.CAT_PICTURE.toString())
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("Гифка \uD83D\uDE40")
                .callbackData(BotInline.CAT_GIF.toString())
                .build();
        listInlineButtons.add(button1);
        listInlineButtons.add(button2);
        listsInlineButtons.add(listInlineButtons);
        inline.setKeyboard(listsInlineButtons);
        return inline;
    }

}
