package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserActivationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public String activation(@RequestParam("id") String id, Model model) {
       model.addAttribute("check",userActivationService.activation(id));
        return "resultRegistration/register";
    }

}
