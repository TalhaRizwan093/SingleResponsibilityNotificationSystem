package com.spring.OAuthSecurity.controller;

import com.spring.OAuthSecurity.dto.LoginRequest;
import com.spring.OAuthSecurity.dto.SignupRequest;
import com.spring.OAuthSecurity.exception.user.UserNotFoundException;
import com.spring.OAuthSecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return userService.authenticate(loginRequest);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> login(@Valid @RequestBody SignupRequest signupRequest){
        return userService.register(signupRequest);
    }

    @GetMapping("/auth/test")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>("Running", HttpStatus.OK);
    }


    @GetMapping("/user/exception")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> testUserException() {
        throw new UserNotFoundException("User");
    }

}
