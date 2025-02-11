package com.pramod.usersservice.repositories;

import com.pramod.usersservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    User save(User user); //upsert

    Optional<User> findByEmail(String email);
}
