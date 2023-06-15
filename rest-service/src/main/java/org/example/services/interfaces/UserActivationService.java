package org.example.services.interfaces;

import org.example.util.MailResultEnum;

public interface UserActivationService {
    MailResultEnum activation(String hashUserId, String encodeMail);
}
