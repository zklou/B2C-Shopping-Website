package com.onlineshop.controller;


import com.onlineshop.service.UserService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/user")
    ResponseEntity<Object> register(@RequestBody RegisterRequest form) {
        return userService.register(
                form.getUsername(),
                form.getEmail(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastname()
        );
    }

    @GetMapping("/user")
    ResponseEntity<Object> getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo((int)request.getAttribute("UserID"));
    }


    @PostMapping("/session")
    ResponseEntity<Object> login(@RequestBody LoginRequest form, HttpServletRequest request) {
        return userService.login(form.getUsername(), form.getPassword(), (String)request.getAttribute("Session"));
    }

    @DeleteMapping("/session")
    ResponseEntity<Object> logout(HttpServletRequest request) {
        return userService.logout((String)request.getAttribute("Session"));
    }


    @Getter
    @Data
    public static class LoginRequest {
        String username;
        String password;
    }

    @Getter
    @Data
    public static class RegisterRequest {
        String username;
        String email;
        String password;
        String firstName;
        String lastname;
    }


}
