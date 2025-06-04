package iti.jets.misk.mappers;

import org.mapstruct.Mapper;

import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.entities.User;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    User userInfoDtotoUser(UserInfoDto infoDto);

    
} 
