package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.TelegramBotMainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@RequestMapping("/callback")
@RestController
public class WebhookBotController {
    private final TelegramBotMainService telegramBotMainService;

    @PostMapping("/update")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        telegramBotMainService.processUpdate(update);
        return ResponseEntity.
                ok().
                build();
    }
}
