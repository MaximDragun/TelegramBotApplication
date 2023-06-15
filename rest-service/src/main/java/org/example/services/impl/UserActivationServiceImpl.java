package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionString;
import org.example.EncryptionTool;
import org.example.models.ApplicationUser;
import org.example.repositories.ApplicationUserRepository;
import org.example.services.interfaces.ProducerService;
import org.example.services.interfaces.UserActivationService;
import org.example.util.MailResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.example.util.MailResultEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EncryptionTool encryptionTool;
    private final EncryptionString encryptionString;
    private final ProducerService producerService;

    @Transactional
    @Override
    public MailResultEnum activation(String hashUserId, String encodeMail) {
        try {
            Long userId = encryptionTool.hashOff(hashUserId);
            String email = encryptionString.decryptURL(encodeMail);
            Optional<ApplicationUser> optionalUserId = applicationUserRepository.findById(userId);
            if (optionalUserId.isPresent()) {
                ApplicationUser applicationUser = optionalUserId.get();
                String newEmailUser = applicationUser.getNewEmail();
                String userEmail = applicationUser.getEmail();
                if (email.equals(newEmailUser) || newEmailUser == null && email.equals(userEmail)) {
                    if (newEmailUser != null) {
                        if (userEmail == null) {
                            applicationUser.setIsActive(true);
                            applicationUser.setEmail(newEmailUser);
                            applicationUser.setNewEmail(null);
                            sendMessageRegistration(applicationUser);
                            return REGISTRATION_SUCCESSFUL;
                        }
                        applicationUser.setEmail(newEmailUser);
                        applicationUser.setNewEmail(null);
                        return CHOOSE_EMAIL_SUCCESSFUL;
                    } else {
                        return RE_REGISTRATION;
                    }
                } else {
                    return MAIL_INVALID;
                }
            } else {
                log.error("Не найден пользователь с Id {}", userId);
                return REGISTRATION_ERROR;
            }
        } catch (Exception e) {
            log.error("Возникла ошибка при расшифровке почты или id пользователя",e);
            return REGISTRATION_ERROR;
        }
    }

    private void sendMessageRegistration(ApplicationUser applicationUser) {
        String chatId = applicationUser.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                """
                        Регистрация прошла успешно! ✅
                        Вам доступны все возможности файлообменника😎
                         """
        );
        sendMessage.setDisableNotification(true);
        producerService.produceAnswer(sendMessage);
    }

}
