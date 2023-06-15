package org.example.configurations;

import org.example.EncryptionString;
import org.example.EncryptionTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public EncryptionTool getEncryptTool() {
        return new EncryptionTool(salt);
    }
    @Bean
    public EncryptionString getEncryptionString() {
        return new EncryptionString(salt);
    }
}
