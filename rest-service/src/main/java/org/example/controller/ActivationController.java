package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        boolean activation = userActivationService.activation(id);
        if (activation) {
            return ResponseEntity
                    .ok()
                    .body("Регистрация прошла успешно!");
        } else return ResponseEntity.internalServerError().body("Активация прошла неудачно! Повторите попытку снова");
    }

}
