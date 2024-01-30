package com.epsi.epsistore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epsi.epsistore.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
    
}
