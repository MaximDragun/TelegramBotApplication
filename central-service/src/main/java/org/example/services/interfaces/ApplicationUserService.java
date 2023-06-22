package org.example.services.interfaces;

import org.example.models.ApplicationUser;

public interface ApplicationUserService {
    String registerUser(ApplicationUser applicationUser);

    String setEmail(ApplicationUser applicationUser, String email);

    String resendEmail(ApplicationUser applicationUser);

    String chooseAnotherEmail(ApplicationUser applicationUser);
}
