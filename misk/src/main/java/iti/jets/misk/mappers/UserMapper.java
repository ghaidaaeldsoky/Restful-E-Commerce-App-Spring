package iti.jets.misk.mappers;

import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
}
