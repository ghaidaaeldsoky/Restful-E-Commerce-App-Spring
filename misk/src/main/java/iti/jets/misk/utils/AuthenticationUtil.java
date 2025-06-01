package iti.jets.misk.utils;

import iti.jets.misk.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public Integer getCurrentUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getUserId();
        } else {
            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
        }
    }
}