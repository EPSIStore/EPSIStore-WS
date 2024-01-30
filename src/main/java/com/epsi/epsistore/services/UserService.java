package com.epsi.epsistore.services;

import com.epsi.epsistore.entities.User;

public interface UserService {

    User findByEmail(String email);
    
}
