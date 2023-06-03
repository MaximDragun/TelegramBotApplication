package org.example.configuration;

import org.example.EncryptionTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Configuration
public class NodeConfiguration {
    @Value("${salt}")
    private String salt;
    @Bean
    public EncryptionTool getEncryptTool() {
        return new EncryptionTool(salt);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();}
    @Scope("prototype")
    @Bean
    public SendMessage sendMessage(){
        return new SendMessage();
    }
}
