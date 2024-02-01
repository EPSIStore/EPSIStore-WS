package com.epsi.epsistore.services;

import com.epsi.epsistore.dtos.AuthResponseDTO;
import com.epsi.epsistore.dtos.RegisterDTO;
import com.epsi.epsistore.entities.User;

public interface UserService {

    User findByEmail(String email);

    AuthResponseDTO register(RegisterDTO registerDto);
    
}
