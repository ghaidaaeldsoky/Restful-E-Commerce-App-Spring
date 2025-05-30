package iti.jets.misk.controllers.users;

import iti.jets.misk.dtos.UserDTO;
import iti.jets.misk.entities.User;
import iti.jets.misk.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("id/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findById(id);
    }

    @GetMapping("email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }
}
