package com.pramod.usersservice.services.impl;

import com.pramod.usersservice.dtos.LoginRequestDto;
import com.pramod.usersservice.dtos.SignupRequestDto;
import com.pramod.usersservice.exceptions.IncorrectPasswordException;
import com.pramod.usersservice.exceptions.UserNotFoundException;
import com.pramod.usersservice.models.Token;
import com.pramod.usersservice.repositories.TokenRepository;
import com.pramod.usersservice.models.User;
import com.pramod.usersservice.repositories.UserRepository;
import com.pramod.usersservice.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User signUp(SignupRequestDto signupRequestDto) {
        User user = new User();
        user.setUsername(signupRequestDto.getUsername());
        user.setEmail(signupRequestDto.getEmail());
        user.setHashedPassword(bCryptPasswordEncoder.encode(signupRequestDto.getPassword()));
        user.setEmailVerified(true);
        return userRepository.save(user);
    }

    @Override
    public Token login(LoginRequestDto user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isEmpty()) {
        throw new UserNotFoundException("User with email "+user.getEmail()+" does not exist");
        }
        User incomingUser = userOptional.get();
        if(!bCryptPasswordEncoder.matches(user.getPassword(), incomingUser.getHashedPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        Token token = generateToken(incomingUser);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {

        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String token =  UUID.randomUUID().toString();
        Token newToken = new Token();
        newToken.setUser(user);
        newToken.setToken(token);
        newToken.setExpireAt(expiryDate);
       return newToken;
    }

    @Override
    public void logout(String token) {
     Optional<Token> optionalToken = tokenRepository.findByTokenAndIsDeleted(token, false);
     if(optionalToken.isEmpty()) {
         return;
     }
        Token ot = optionalToken.get();
        ot.setDeleted(true);
        tokenRepository.save(ot);
    }

    @Override
    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByTokenAndIsDeletedAndExpireAtGreaterThan(token, false, new Date());
        if(optionalToken.isEmpty()) {
            throw new UserNotFoundException("User with valid token "+token+" does not exist");
        }


        return optionalToken.get().getUser();
    }


}
