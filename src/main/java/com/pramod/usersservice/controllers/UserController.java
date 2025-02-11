package com.pramod.usersservice.controllers;

import com.pramod.usersservice.dtos.LoginRequestDto;
import com.pramod.usersservice.dtos.LogoutRequestDto;
import com.pramod.usersservice.dtos.SignupRequestDto;
import com.pramod.usersservice.dtos.UserDto;
import com.pramod.usersservice.models.Token;
import com.pramod.usersservice.models.User;
import com.pramod.usersservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {

        User user = userService.signUp(signupRequestDto);

        return UserDto.fromUser(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) {

        return userService.login(loginRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
       userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {

        return UserDto.fromUser(userService.validateToken(token));
    }
}
