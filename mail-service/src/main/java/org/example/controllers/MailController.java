package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.MailDTO;
import org.example.services.interfaces.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
