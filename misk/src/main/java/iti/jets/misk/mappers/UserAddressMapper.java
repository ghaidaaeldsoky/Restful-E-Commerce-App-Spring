package iti.jets.misk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import iti.jets.misk.dtos.UserAddressDto;
import iti.jets.misk.entities.Useraddress;


@Mapper(componentModel = "spring")
public interface UserAddressMapper {

 
      UserAddressDto userAddresstoUserAddressDto(Useraddress address);
}