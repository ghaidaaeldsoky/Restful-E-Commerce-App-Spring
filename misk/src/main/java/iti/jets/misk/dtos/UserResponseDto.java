package iti.jets.misk.dtos;

import java.io.Serializable;
import java.util.List;

public class UserResponseDto {

    List<UserDTO> users;

    public UserResponseDto() {
    }

    public UserResponseDto(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

}
