package iti.jets.misk.services;

import iti.jets.misk.dtos.LoginRequest;
import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.User;
import iti.jets.misk.entities.Useraddress;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.mappers.UserInfoMapper;
import iti.jets.misk.mappers.UserMapper;
import iti.jets.misk.repositories.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

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

    @Transactional
    public Set<Order> getAllOrderByUserId(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User With ID" + id + "not found"));
        return user.getOrders();

    }
    @Transactional
    public Set<Useraddress> getUserAddresses(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User With ID" + id + "not found"));
        return user.getUseraddresses();

    }
     public User saveUser(UserInfoDto dto)
    {
        User user = userInfoMapper.userInfoDtotoUser(dto);

        return userRepository.save(user);
    }

     BigDecimal getUserCreditCardLimit(int id)
     {
        return userRepository.getUserCreditCardLimit(id);
     }

    void updateCreditLimit(int userId,BigDecimal newLimit)
    {
             userRepository.updateCreditLimit(userId,newLimit);
    }

     public boolean addListOfAddresses(int id, List<Useraddress> address)
     {
      return  userRepository.addListOfAddresses(id, address);
     }

     public boolean checkEmailAvilibity(String email)
     {
        return userRepository.findByEmail(email).isPresent();
     }

     
    public boolean validateUser(LoginRequest loginRequest)
    {
        User user = userRepository.findByEmail(loginRequest.email()).get();

       return loginRequest.email().equals(user.getEmail())&&loginRequest.password().equals(user.getPassword());
    }

   

     





}
