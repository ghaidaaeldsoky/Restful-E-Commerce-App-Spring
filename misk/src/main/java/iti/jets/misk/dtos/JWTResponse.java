package iti.jets.misk.dtos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class JWTResponse {

    @NonNull
    String token;

    @NonNull
    String type;

  //  @Value("${security.jwtExpirationInSeconds}")
    long expiredDate = 28800;

    @NonNull
    String role ;

}
