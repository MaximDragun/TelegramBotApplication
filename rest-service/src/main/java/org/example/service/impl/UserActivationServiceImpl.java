package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionTool;
import org.example.exceptions.NotFoundException;
import org.example.model.ApplicationUser;
import org.example.repository.ApplicationUserRepository;
import org.example.service.ProducerService;
import org.example.service.UserActivationService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Slf4j
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
            if (applicationUser.getIsActive()) {
                sendMessageRegistrationRepeat(applicationUser);
                return true;
            }
            applicationUser.setIsActive(true);
            applicationUserRepository.save(applicationUser);
            sendMessageRegistration(applicationUser);
            return true;
        } else {
            log.error("Не найден пользователь с Id {}", userId);
            return false;
        }
    }

    private void sendMessageRegistration(ApplicationUser applicationUser) {
        String chatId = applicationUser.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                """
                        Регистрация прошла успешно! ✅
                        Вам доступны все возможности файлообменника.
                        Загружайте фотографии и документы бесплатно и без ограничений 😎
                         """
        );
        producerService.produceAnswer(sendMessage);
    }
    private void sendMessageRegistrationRepeat(ApplicationUser applicationUser) {
        String chatId = applicationUser.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                """
                        Вы уже были зарегистрированы 😳
                        Ссылку на почте больше нажимать не обязательно.. 🙈
                         """
        );
        producerService.produceAnswer(sendMessage);
    }
}
