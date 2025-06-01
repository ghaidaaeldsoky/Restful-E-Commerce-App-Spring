package iti.jets.misk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//*********************added for simulation*****************************
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home"; // resources/templates/home.html
    }
}