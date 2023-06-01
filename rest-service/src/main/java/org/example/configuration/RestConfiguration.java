package org.example.configuration;

import org.example.EncryptionTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public EncryptionTool getEncryptTool() {
        return new EncryptionTool(salt);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();}
}
