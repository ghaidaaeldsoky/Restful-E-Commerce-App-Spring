package iti.jets.misk.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import iti.jets.misk.entities.User;

@Service
public class JWTProvider {

    @Value("${security.jwtExpirationInSeconds}")
    private Long jwtExpirationInSecond;

    @Autowired
    JwtEncoder encoder ;

    @Autowired
    UserService service;

    public String JWTManager(String email)
    {

        User user = service.findByEmail(email);

        String subject = Integer.toString(user.getUserId());

        Map<String,Object> claims = new HashMap<>();

        if(user.isIsAdmin())
        {
            claims.put("authorities",List.of("ADMIN"));
        }
        else 
        {
             claims.put("authorities", List.of("USER"));
        }

        return JWTGeneration(subject, claims);


    

    }

    public String JWTGeneration(String subject ,Map<String,Object> claims)
    {
        var claimSet = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
        .subject(subject)
        .claims(c->c.putAll(claims))
        .build();

        return encoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

}
