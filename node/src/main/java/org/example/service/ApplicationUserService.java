package org.example.service;

import org.example.model.ApplicationUser;

public interface ApplicationUserService {
    String registerUser(ApplicationUser applicationUser);

    String setEmail(ApplicationUser applicationUser, String email);

    String resendEmail(ApplicationUser applicationUser);

    String chooseAnotherEmail(ApplicationUser applicationUser);
}
