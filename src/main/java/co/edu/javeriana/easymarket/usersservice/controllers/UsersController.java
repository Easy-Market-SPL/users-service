package co.edu.javeriana.easymarket.usersservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @GetMapping
    public String getUsers() {
        return "Hello from Users Service local";
    }
}
