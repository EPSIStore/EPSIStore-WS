package com.epsi.epsistore.services;

import com.epsi.core.entities.User;
import com.epsi.epsistore.dtos.RegisterDTO;

public interface UserService {

    User findByEmail(String email);
    User findById(int id);

    String register(RegisterDTO registerDto);
}