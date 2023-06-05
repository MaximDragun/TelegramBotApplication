package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionTool;
import org.example.model.ApplicationUser;
import org.example.repository.ApplicationUserRepository;
import org.example.service.interfaces.ProducerService;
import org.example.service.interfaces.UserActivationService;
import org.example.utils.MailResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.example.utils.MailResultEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EncryptionTool encryptionTool;
    private final ProducerService producerService;

    @Transactional
    @Override
    public MailResultEnum activation(String hashUserId) {
        Long userId = encryptionTool.hashOff(hashUserId);
        Optional<ApplicationUser> optionalUserId = applicationUserRepository.findById(userId);
        if (optionalUserId.isPresent()) {
            ApplicationUser applicationUser = optionalUserId.get();
            if (applicationUser.getIsActive()) {
                return RE_REGISTRATION;
            }
            applicationUser.setIsActive(true);
            sendMessageRegistration(applicationUser);
            return REGISTRATION_SUCCESSFUL;
        } else {
            log.error("Не найден пользователь с Id {}", userId);
            return REGISTRATION_ERROR;
        }
    }

    private void sendMessageRegistration(ApplicationUser applicationUser) {
        String chatId = applicationUser.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setDisableNotification(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                """
                        Регистрация прошла успешно! ✅
                        Вам доступны все возможности файлообменника.
                        Загружайте фотографии и документы бесплатно и без ограничений 😎
                         """
        );
        sendMessage.setDisableNotification(true);
        producerService.produceAnswer(sendMessage);
    }

}
