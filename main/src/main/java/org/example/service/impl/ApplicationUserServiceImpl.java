package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionTool;
import org.example.dto.MailDTO;
import org.example.model.ApplicationUser;
import org.example.repository.ApplicationUserRepository;
import org.example.service.interfaces.ApplicationUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

import static org.example.model.enums.UserState.BASIC_STATE;
import static org.example.model.enums.UserState.WAIT_FOR_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EncryptionTool encryptionTool;
    private final RestTemplate restTemplate;
    @Value("${service.mail.uri}")
    private String mailServiceUri;

    @Transactional
    @Override
    public String registerUser(ApplicationUser applicationUser) {
        if (applicationUser.getIsActive()) {
            return "Вы уже зарегистрированы!";
        } else if (applicationUser.getEmail() != null) {
            return """
                    Вам на почту отправлено письмо.
                    Перейдите по ссылке для окончания регистрации.
                    Для повторной отправки письма введите /resend_email
                    Для изменения введенной почты введите /choose_another_email""";
        }
        applicationUser.setUserState(WAIT_FOR_EMAIL);
        applicationUserRepository.save(applicationUser);
        return "Введите ваш email:";
    }

    @Transactional
    @Override
    public String setEmail(ApplicationUser applicationUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return "Введите корректный email! Для отмены команды введите /cancel";
        }
        Optional<ApplicationUser> byEmail = applicationUserRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            applicationUser.setEmail(email);
            applicationUser.setUserState(BASIC_STATE);
            ApplicationUser user = applicationUserRepository.save(applicationUser);
            String hashId = encryptionTool.hashOn(user.getId());
            ResponseEntity<String> response = sendRequestToMailService(hashId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                String msg = String.format("Отправка эл. письма на почту %s не удалась", email);
                log.error(msg);
                applicationUser.setEmail(null);
                applicationUserRepository.save(applicationUser);
                return msg;
            }
            return "Вам на почту отправлено письмо. " +
                    "Перейдите по ссылке в письме для подтверждения регистрации";
        } else {
            return "Этот email уже используется, введите другой email";
        }

    }

    @Override
    public String resendEmail(ApplicationUser applicationUser) {
        if (applicationUser.getIsActive())
            return "Пользователь уже зарегистрирован, повторная отправка не требуется";
        else if (applicationUser.getEmail() == null) {
            return "Используйте команду /registration для указания вашей почты";
        } else {
            String hashId = encryptionTool.hashOn(applicationUser.getId());
            ResponseEntity<String> response = sendRequestToMailService(hashId, applicationUser.getEmail());
            if (response.getStatusCode() != HttpStatus.OK) {
                String msg = String.format("Отправка эл. письма на почту %s не удалась", applicationUser.getEmail());
                log.error(msg);
                return msg;
            }
            return "Вам на почту повторно отправлено письмо. " +
                    "Перейдите по ссылке в письме для подтверждения регистрации";
        }

    }

    @Transactional
    @Override
    public String chooseAnotherEmail(ApplicationUser applicationUser) {
        if (applicationUser.getEmail() == null) {
            return "Используйте команду /registration для указания вашей почты";
        }
        applicationUser.setUserState(WAIT_FOR_EMAIL);
        applicationUserRepository.save(applicationUser);
        return "Введите новый email:";
    }

    private ResponseEntity<String> sendRequestToMailService(String hashId, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MailDTO mailParam = MailDTO.builder()
                .id(hashId)
                .emailTo(email)
                .build();
        HttpEntity<MailDTO> mailDTOHttpEntity = new HttpEntity<>(mailParam, headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                mailDTOHttpEntity,
                String.class);
    }
}
