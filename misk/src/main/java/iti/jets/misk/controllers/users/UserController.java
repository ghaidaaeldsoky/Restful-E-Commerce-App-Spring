package iti.jets.misk.controllers.users;

import iti.jets.misk.dtos.LoginRequest;
import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.UserAlreadyExistException;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.services.JWTProvider;
import iti.jets.misk.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired 
    private JWTProvider JwtProvider;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<User>addNewUser(@RequestBody UserInfoDto dto)
    {
        if(userService.checkEmailAvilibity(dto.email()))
        {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, new UserAlreadyExistException("you have an already account"));
        }

        User user = userService.saveUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);




    }
    // login
    @PostMapping("/login")
    public ResponseEntity<String> createJWTToken(@RequestBody LoginRequest loginRequest )
    {
        if(!userService.checkEmailAvilibity(loginRequest.email()))
        {
            throw new UserNotFoundException("this is user is not found");
        }

         if(!userService.validateUser(loginRequest))
        {
            throw new UserNotFoundException("the email or password may be wrong");
        }

        String token = JwtProvider.JWTManager(loginRequest.email());

        return ResponseEntity.ok().body(token);

    }
}
