package org.example.services.interfaces;

import org.example.models.ApplicationUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
     void processTextMessage(Update update);
     void processDocumentMessage(Update update);
     void processPhotoMessage(Update update);
     void processCallBack(Update update);
      void saveMessage(Update update);
    String cancelProcess(ApplicationUser applicationUser);
     ApplicationUser findOrSaveApplicationUser(Update update);
}
