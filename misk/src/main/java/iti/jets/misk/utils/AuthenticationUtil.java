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

        String id = auth.getName();

        return Integer.parseInt(id);



      
    }
    
  

 
}