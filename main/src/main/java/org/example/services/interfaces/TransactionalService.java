package org.example.services.interfaces;

import org.example.models.ApplicationUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TransactionalService {
    String cancelProcess(ApplicationUser applicationUser);

    ApplicationUser findOrSaveApplicationUser(Update update);

    void saveMessage(Update update);

}
