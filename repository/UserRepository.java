package com.example.authsystem.repository;

import com.example.authsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmailOrUsernameOrPhoneNumber(
        String email,
        String username,
        String phoneNumber
);

}
