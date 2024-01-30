package com.epsi.epsistore.services.impls;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epsi.epsistore.entities.User;
import com.epsi.epsistore.repositories.UserRepository;
import com.epsi.epsistore.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String username) {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }
}
