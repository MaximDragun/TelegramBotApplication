package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.MailDTO;
import org.example.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/mail")
@RestController
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailDTO mail) {
        mailService.send(mail);
        return ResponseEntity
                .ok()
                .build();
    }
}