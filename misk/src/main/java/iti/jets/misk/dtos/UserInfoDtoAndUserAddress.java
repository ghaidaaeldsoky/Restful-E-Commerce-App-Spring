package iti.jets.misk.dtos;

import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import iti.jets.misk.entities.Useraddress;

public record UserInfoDtoAndUserAddress(UserInfoDto userInfoDto,List<UserAddressDto>  useraddress) {
}