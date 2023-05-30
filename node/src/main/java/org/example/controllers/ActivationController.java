package org.example.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {

    @PostMapping("/activation")
    public void activation(@RequestBody String result){
        
    }
}
