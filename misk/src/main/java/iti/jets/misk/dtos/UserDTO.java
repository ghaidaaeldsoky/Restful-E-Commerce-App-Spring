package iti.jets.misk.dtos;

import java.math.BigDecimal;
import java.sql.Date;

public record UserDTO(String name, String phoneNumber, String email, Date birthday, String job, BigDecimal creditLimit, String interests) {
    // The canonical constructor is created by default
}
