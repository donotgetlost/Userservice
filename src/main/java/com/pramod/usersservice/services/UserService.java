package com.pramod.usersservice.services;

import com.pramod.usersservice.dtos.LoginRequestDto;
import com.pramod.usersservice.dtos.SignupRequestDto;
import com.pramod.usersservice.models.Token;
import com.pramod.usersservice.models.User;

public interface UserService {
    public User signUp(SignupRequestDto user);

    public Token login(LoginRequestDto user);

    public void logout(String token);

    public User validateToken(String token);
}
