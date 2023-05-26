package org.example.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.UpdateProducer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
@Slf4j
public class UpdateProducerImpl implements UpdateProducer {
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.info(update.getMessage().getText());
        log.info("Хочу хочу хрочу");
    }
}
