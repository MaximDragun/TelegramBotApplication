package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserActivationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public ResponseEntity<String> activation(@RequestParam("id") String id) {
        boolean activation = userActivationService.activation(id);
        if (activation)
            return ResponseEntity.ok("Регистрация прошла успешно!");
        return ResponseEntity.internalServerError().body("Регистрация завершилась с ошибкой! Попробуйте снова");
    }

}
