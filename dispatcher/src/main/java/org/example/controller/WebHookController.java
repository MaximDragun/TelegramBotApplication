package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.TelegramBotMainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
@RequiredArgsConstructor
@RequestMapping("/callback")
@RestController
public class WebHookController {
    private final TelegramBotMainService telegramBotMainService;

    @PostMapping("/search")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        telegramBotMainService.processUpdate(update);
        return ResponseEntity.ok().build();
    }
}
