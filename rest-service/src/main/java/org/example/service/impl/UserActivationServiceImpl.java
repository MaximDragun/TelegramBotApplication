package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.EncryptionTool;
import org.example.model.ApplicationUser;
import org.example.repository.ApplicationUserRepository;
import org.example.service.UserActivationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EncryptionTool encryptionTool;

    @Override
    public boolean activation(String hashUserId) {
        Long userId = encryptionTool.hashOff(hashUserId);
        Optional<ApplicationUser> optionalUserId = applicationUserRepository.findById(userId);
        if (optionalUserId.isPresent()) {
            ApplicationUser applicationUser = optionalUserId.get();
            applicationUser.setIsActive(true);
            applicationUserRepository.save(applicationUser);
            return true;
        }
        return false;
    }
}
