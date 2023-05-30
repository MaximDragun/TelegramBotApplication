package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.EncryptionTool;
import org.example.model.ApplicationUser;
import org.example.repository.ApplicationUserRepository;
import org.example.repository.MessageRepository;
import org.example.service.ProducerService;
import org.example.service.UserActivationService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EncryptionTool encryptionTool;
    private final ProducerService producerService;

    @Override
    public boolean activation(String hashUserId) {
        Long userId = encryptionTool.hashOff(hashUserId);
        Optional<ApplicationUser> optionalUserId = applicationUserRepository.findById(userId);
        if (optionalUserId.isPresent()) {
            ApplicationUser applicationUser = optionalUserId.get();
            applicationUser.setIsActive(true);
            applicationUserRepository.save(applicationUser);
            sendMessageRegistration(applicationUser);
            return true;
        }
        return false;
    }

    private void sendMessageRegistration(ApplicationUser applicationUser) {
        String chatId = applicationUser.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                """
                        Регистрация прошла успешно!
                        Вам доступны возможности файлообменника.
                        Загружайте фотографии и документы бесплатно и без ограничений.
                         """
        );
        producerService.produceAnswer(sendMessage);
    }
}
