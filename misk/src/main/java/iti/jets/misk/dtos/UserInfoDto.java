package iti.jets.misk.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String userName;
    private String phoneNumber;
    private String email;
    private String birthDay;
    private String job;
    private String creditLimit;
    private String intersets;
    private String password;}