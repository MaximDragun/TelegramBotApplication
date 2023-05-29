package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.MailDTO;
import org.example.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String serviceUri;

    @Override
    public void send(MailDTO mail) {
        String subject = "Активация учетной записи пользователя";
        String messageBody = getActivateEmailBody(mail.getId());
        String mailTo = mail.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    private String getActivateEmailBody(String id) {
        String msg = String.format("Для завершения регистрации перейдите по ссылке:\n%s",
                serviceUri);
        return msg.replace("{id}", id);
    }
}
