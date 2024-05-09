package com.epsi.epsistore.services.Impls;


import com.epsi.core.entities.Role;
import com.epsi.core.entities.User;
import com.epsi.core.repositories.RoleRepository;
import com.epsi.core.repositories.UserRepository;
import com.epsi.epsistore.dtos.RegisterDTO;
import com.epsi.epsistore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User findByEmail(String username) {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

    @Override
    public String register(RegisterDTO registerDto) {
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
                .pwd(passwordEncoder.encode(registerDto.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        return "ok";
    }

}