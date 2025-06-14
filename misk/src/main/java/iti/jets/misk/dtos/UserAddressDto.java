package iti.jets.misk.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserAddressDto {

    
     private Integer addressId;
  
     private String state;

     private String city;
     
     private String street;

     private Long departmentNumber;

}
