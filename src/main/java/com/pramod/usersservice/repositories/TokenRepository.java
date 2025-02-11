package com.pramod.usersservice.repositories;

import com.pramod.usersservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);

    Optional<Token> findByTokenAndIsDeleted(String token, boolean deleted);

    Optional<Token> findByTokenAndIsDeletedAndExpireAtGreaterThan(String token, boolean deleted, Date expireAt);
}