package iti.jets.misk.controllers.users;

import iti.jets.misk.dtos.ApiResponse;
import iti.jets.misk.dtos.JWTResponse;
import iti.jets.misk.dtos.LoginRequestDto;
import iti.jets.misk.dtos.PasswordRequest;
import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.dtos.UserInfoDto;
import iti.jets.misk.dtos.UserInfoDtoAndUserAddress;
import iti.jets.misk.dtos.UserResponseDto;
import iti.jets.misk.entities.User;
import iti.jets.misk.exceptions.UserAlreadyExistException;
import iti.jets.misk.exceptions.UserNotFoundException;
import iti.jets.misk.services.JWTProvider;
import iti.jets.misk.services.UserService;
import iti.jets.misk.utils.AuthenticationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.ErrorResponseException;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired 
    private JWTProvider JwtProvider;

      @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto>>getAllUsers()
    {
   
        List<UserDTO> userDTOs =userService.findAll();

        UserResponseDto userResponseDto = new UserResponseDto(userDTOs);

        return ResponseEntity.ok(ApiResponse.success(userResponseDto));



    }
 

 @GetMapping("/profile")
         @PreAuthorize("hasAuthority('USER')")
public ResponseEntity<ApiResponse<UserInfoDtoAndUserAddress>> getUser() {

         int id = Integer.parseInt( SecurityContextHolder.getContext().getAuthentication().getName()) ;

    
    UserInfoDtoAndUserAddress user= userService.findById(id);

    return ResponseEntity.ok(ApiResponse.success(user));
    }

    

    @GetMapping("email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        User user= userService.findByEmail(email);

         return ResponseEntity.ok(ApiResponse.success(user));
    }



      
    @PatchMapping()
    public ResponseEntity<ApiResponse<String>> updateUser( @RequestBody User newUser) {
        try {
            
         int id = Integer.parseInt( SecurityContextHolder.getContext().getAuthentication().getName()) ;
            userService.updateUser(id, newUser);
            return ResponseEntity.ok(ApiResponse.success("user updated Succefully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error("cannot update user"));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<User>addNewUser(@RequestBody UserInfoDto dto)
    {
        if(userService.checkEmailAvilibity(dto.getEmail()))
        {
            throw new UserAlreadyExistException("you have an already account");
        }

        User user = userService.saveUser(dto);
        

        return ResponseEntity.status(HttpStatus.CREATED).body(user);





    }

    @PostMapping("/chnagePassword")
     @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<User>> chnagePassword(@RequestBody PasswordRequest passwordRequest )
    {
       try{  int id = Integer.parseInt( SecurityContextHolder.getContext().getAuthentication().getName()) ;

        System.out.println(passwordRequest.getNewPassword());
        System.out.println(passwordRequest.getOldPassword());

      userService.updatePassword(id, passwordRequest);

      return ResponseEntity.ok(ApiResponse.success("user updated Succefully",null));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("cannot update user"));
        }
    }
 
    
    // login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JWTResponse>> createJWTToken(@RequestBody LoginRequestDto loginRequest )
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

        String role = userService.getUserRole(loginRequest.email());

        JWTResponse jwtResponse= new JWTResponse(token, "Bearer",role);





        return ResponseEntity.ok().body(ApiResponse.success(jwtResponse));


    }

    

   
}
