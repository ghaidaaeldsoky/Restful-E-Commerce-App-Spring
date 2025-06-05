package iti.jets.misk.mappers;

import org.mapstruct.Mapper;

import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.entities.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {


    @Mapping(target = "name", source = "userName")
    User userInfoDtotoUser(UserInfoDto infoDto);



    
} 
