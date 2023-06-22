package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.ApplicationUser;
import org.example.models.MessageEntity;
import org.example.repositories.ApplicationUserRepository;
import org.example.repositories.MessageRepository;
import org.example.services.interfaces.TransactionalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static org.example.models.enums.UserState.BASIC_STATE;

@RequiredArgsConstructor
@Transactional
@Service
public class TransactionalServiceImpl implements TransactionalService {
    private final MessageRepository messageRepository;
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public String cancelProcess(ApplicationUser applicationUser) {
        applicationUser.setUserState(BASIC_STATE);
        applicationUserRepository.save(applicationUser);
        return "Команда отменена";
    }

    @Override
    public ApplicationUser findOrSaveApplicationUser(Update update) {
        User user = update.getMessage().getFrom();
        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByTelegramUserId(user.getId());
        if (applicationUser.isPresent())
            return applicationUser.get();
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

    @Override
    public void saveMessage(Update update) {
        MessageEntity build = MessageEntity.builder()
                .update(update)
                .build();
        messageRepository.save(build);
    }
}
