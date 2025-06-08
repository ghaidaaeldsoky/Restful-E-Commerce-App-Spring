package iti.jets.misk.services;

import iti.jets.misk.dtos.LoginRequestDto;
import iti.jets.misk.dtos.PasswordRequest;
import iti.jets.misk.dtos.UserAddressDto;
import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.dtos.UserInfoDtoAndUserAddress;
import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.User;
import iti.jets.misk.entities.Useraddress;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.exceptions.UserRegisterationException;
import iti.jets.misk.mappers.UserAddressMapper;
import iti.jets.misk.mappers.UserInfoMapper;
import iti.jets.misk.mappers.UserMapper;
import iti.jets.misk.repositories.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.thymeleaf.util.Validate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder encoder;

      @Autowired
    private UserAddressMapper addressMapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

  
    public UserInfoDtoAndUserAddress findById(int id) {
        User user= userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
            
            UserInfoDto dto = userInfoMapper.usertoUserInfoDto(user);

            dto.setPassword(null);

            List<UserAddressDto> addressDtos = user.getUseraddresses().stream()
            
            .map(addressMapper::userAddresstoUserAddressDto)
            .collect(Collectors.toList());

            ;

            return new UserInfoDtoAndUserAddress(dto, addressDtos);

                

                
    }

    public User findByEmail(String email) throws IllegalArgumentException{
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with Email " + email + " not found"));
    }

    public void updateUser(int id, User newUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        if (newUser.getEmail() != null)  existingUser.setEmail(newUser.getEmail());

        if (newUser.getName() != null)  existingUser.setName(newUser.getName());

        if (newUser.getPhoneNumber() != null)  existingUser.setPhoneNumber(newUser.getPhoneNumber());

    //   if (newUser.getPassword() != null) 
    //   {
    //     boolean isValid = validatePassword(newUser.getPassword(),existingUser.getPassword());
    //     if(isValid)
    //    existingUser.setPassword(newUser.getPassword());

    //    else
    //    throw new IllegalArgumentException("cannot change the password ");

    //   }

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
        try {
            User user = userInfoMapper.userInfoDtotoUser(dto);

           user.setPassword(encoder.encode(dto.getPassword()));

            return userRepository.save(user);
        }
        catch(Exception e){
            throw new UserRegisterationException("Failed to create user");
        }
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

     public String FindEmailByUserId(int id)
     {
         User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User With ID" + id + "not found"));
        return user.getEmail();
     }

     
    public boolean validateUser(LoginRequestDto loginRequest)
    {
        User user = userRepository.findByEmail(loginRequest.email()).get();

    boolean isValid= validatePassword(loginRequest.password(),user.getPassword());

       return loginRequest.email().equals(user.getEmail())&&isValid;
    }


    public String getUserRole(String email)
    {
        Optional<User> optinalUser= userRepository.findByEmail(email);
       
        User user = optinalUser.get();


        return user.isIsAdmin()?"ADMIN":"USER";

        


    }



private boolean validatePassword(String rawPassword, String encodedPassword) {
    return encoder.matches(rawPassword, encodedPassword);
}

public void updatePassword(int userId, PasswordRequest passwordRequest) {
  User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

    boolean isValid = validatePassword(passwordRequest.getOldPassword(), existingUser.getPassword());

    if (isValid) {
       
        String hashedPassword = encoder.encode(passwordRequest.getNewPassword());
        System.out.println(hashedPassword);
        existingUser.setPassword(hashedPassword);
    } else {
        throw new IllegalArgumentException("Cannot change the password: old password doesn't match");
    }

     userRepository.save(existingUser);
}


     





}
