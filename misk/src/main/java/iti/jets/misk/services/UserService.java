package iti.jets.misk.services;

import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.mappers.UserMapper;
import iti.jets.misk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with Email " + email + " not found"));
    }

    public void updateUser(int id, User newUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        if (newUser.getEmail() != null)  existingUser.setEmail(newUser.getEmail());

        if (newUser.getName() != null)  existingUser.setName(newUser.getName());

        if (newUser.getPhoneNumber() != null)  existingUser.setPhoneNumber(newUser.getPhoneNumber());

        if (newUser.getPassword() != null)  existingUser.setPassword(newUser.getPassword());

        if (newUser.getBirthday() != null)  existingUser.setBirthday(newUser.getBirthday());

        if (newUser.getJob() != null)  existingUser.setJob(newUser.getJob());

        if (newUser.getCreditLimit() != null)  existingUser.setCreditLimit(newUser.getCreditLimit());

        if (newUser.getInterests() != null)  existingUser.setInterests(newUser.getInterests());

        if (newUser.isIsAdmin() != existingUser.isIsAdmin())  existingUser.setIsAdmin(newUser.isIsAdmin());

        if (newUser.getOrders() != null && !newUser.getOrders().isEmpty())  existingUser.setOrders(newUser.getOrders());

        userRepository.save(existingUser);
    }
}
