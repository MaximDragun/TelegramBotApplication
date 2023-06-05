package org.example.service.interfaces;

import org.example.utils.MailResultEnum;

public interface UserActivationService {
    MailResultEnum activation(String hashUserId);
}
