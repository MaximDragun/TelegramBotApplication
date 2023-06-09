package org.example.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.models.RabbitQueue.*;


@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue phototMessageQueue() {
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue documentMessageQueue() {
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    @Bean
    public Queue callBackQueue() {
        return new Queue(CALLBACK_QUERY_UPDATE);
    }
    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE);
    }

    @Bean
    public Queue answerPhotoQueue() {
        return new Queue(ANSWER_PHOTO);
    }
    @Bean
    public Queue answerAnimationQueue() {
        return new Queue(ANSWER_ANIMATION);
    }

}
