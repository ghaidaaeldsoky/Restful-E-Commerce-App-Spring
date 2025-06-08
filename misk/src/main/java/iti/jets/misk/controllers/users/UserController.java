package iti.jets.misk.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import org.springframework.web.bind.annotation.*;

import org.springframework.web.ErrorResponseException;


import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired 
    private JWTProvider JwtProvider;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("id/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findById(id);
    }


    @Operation(summary = "Get user by email")
    @GetMapping("email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }


    @Operation(summary = " Update user by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User newUser) {
        try {
            userService.updateUser(id, newUser);
            return ResponseEntity.ok("User profile updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "add new user by providing user info")
    @PostMapping("/register")
    public ResponseEntity<User>addNewUser(@RequestBody UserInfoDto dto)
    {
        if(userService.checkEmailAvilibity(dto.email()))
        {
            throw new UserAlreadyExistException("you have an already account");
        }

        User user = userService.saveUser(dto);
        

        return ResponseEntity.status(HttpStatus.CREATED).body(user);




    }
    // login
    @Operation(summary = "login user by providing email and password")
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
