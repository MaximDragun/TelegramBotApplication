package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.EncryptionString;
import org.example.dto.MailDTO;
import org.example.services.interfaces.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final SpringTemplateEngine springTemplateEngine;
    private final JavaMailSender javaMailSender;
    private final EncryptionString encryptionString;

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String serviceUri;
    @Value("${email.template.location}")
    private String emailTemplateLocation;
    @Value("${bot.uri}")
    private String botUri;

    @Override
    public void send(MailDTO mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        Map<String, Object> mapContext = new HashMap<>();
        String mailDecode = mail.getEmailTo();
        String idDecode = mail.getId();
        try {
            String encodeEmail = encryptionString.decrypt(mailDecode);
            String emailForUrl = encryptionString.encryptURL(encodeEmail);

            mapContext.put("emailTo", serviceUri.replace("{id}", idDecode).replace("{mail}", emailForUrl));
            mapContext.put("linkToBot", botUri);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String subject = "Активация учетной записи пользователя";
            Context context = new Context();
            context.setVariables(mapContext);
            String emailContent = springTemplateEngine.process(emailTemplateLocation, context);

            mimeMessageHelper.setTo(encodeEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(emailFrom);
            mimeMessageHelper.setText(emailContent, true);
        } catch (MessagingException e) {
            log.error("Не удалось отправить сообщение пользователю с Id: {} на Email: {}", idDecode, mailDecode, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось отправить сообщение на указанную почту", e);
        }
        javaMailSender.send(message);
    }
}
