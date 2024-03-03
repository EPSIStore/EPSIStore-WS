package com.epsi.epsistore.services.Impls;

import com.epsi.core.entities.User;
import com.epsi.epsistore.configs.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.epsi.epsistore.services.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + username);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);

        return customUserDetails;
    }
}