package iti.jets.misk.mappers;

import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")  //componentModel = "spring" ==> must be exist or it will cause an error
public interface UserMapper {
    UserDTO userToUserDTO(User user);

     User userDTOToUser(UserDTO user);
}
