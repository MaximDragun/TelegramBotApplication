package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.MessageEntity;
import org.example.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{
    private final MessageRepository messageRepository;
    private final ProducerService producerService;
    @Override
    public void processTextMessage(Update update){
        saveMessage(update);

        ThreadLocalRandom tr = ThreadLocalRandom.current();
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Сегодня: "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy HH:mm", Locale.of("RU")))+"\n" +
                "За окном температура тепла: " + tr.nextInt(0,10)+" градусов");
        producerService.produceAnswer(sendMessage);
    }

    private void saveMessage(Update update) {
        MessageEntity build = MessageEntity.builder()
                .update(update)
                .build();
        messageRepository.save(build);
    }
}
