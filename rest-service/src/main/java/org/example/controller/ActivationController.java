package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.interfaces.UserActivationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public String activation(@RequestParam("id") String id, @RequestParam("mail") String encodeMail) {
        return switch (userActivationService.activation(id, encodeMail)) {
            case REGISTRATION_ERROR -> "resultRegistration/error";
            case REGISTRATION_SUCCESSFUL -> "resultRegistration/register";
            case RE_REGISTRATION -> "resultRegistration/pressing_again";
            case CHOOSE_EMAIL_SUCCESSFUL -> "resultRegistration/new";
            case MAIL_INVALID -> "resultRegistration/invalid";
        };
    }
}
