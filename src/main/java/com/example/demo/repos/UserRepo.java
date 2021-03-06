package com.example.demo.repos;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findOneById(int id);
    Optional<User> findByUsername(String username);
}
