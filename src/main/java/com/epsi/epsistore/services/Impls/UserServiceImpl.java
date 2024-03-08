package com.epsi.epsistore.services.Impls;


import com.epsi.core.entities.Role;
import com.epsi.core.entities.User;
import com.epsi.core.repositories.RoleRepository;
import com.epsi.core.repositories.UserRepository;
import com.epsi.epsistore.configs.CustomUserDetails;
import com.epsi.epsistore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epsi.epsistore.configs.JwtUtils;
import com.epsi.epsistore.dtos.AuthResponseDTO;
import com.epsi.epsistore.dtos.RegisterDTO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;


    @Override
    public User findByEmail(String username) {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

    // method configuring user registration
    @Override
    public AuthResponseDTO register(RegisterDTO registerDto) {
        String roleName = "ROLE_USER";

        // define the role
        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(roleName);
                    return roleRepository.save(newRole);
                });

        // create a BCryptPasswordEncoder object to encode passwords
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // creation of the user with his information
        var user = User.builder()
                .email(registerDto.getUsername())
                .pwd(passwordEncoder.encode(registerDto.getPwd()))
                .role(role)
                .build();
        userRepository.save(user);

        // create an instance of CustomUserDetails
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);

        // generation and return of the token
        var jwtToken = jwtUtils.generateToken(customUserDetails);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();
    }


}