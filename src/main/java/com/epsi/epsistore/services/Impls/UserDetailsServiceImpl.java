package com.epsi.epsistore.services.Impls;

import com.epsi.core.entities.Role;
import com.epsi.epsistore.entity.UserDetailsImpl;
import com.epsi.epsistore.services.RoleService;
import com.epsi.epsistore.services.UserDetailsServiceImp;
import com.epsi.epsistore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsServiceImp {
    private final UserService userService;
    private final RoleService roleService;

    // allows loading user details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(this, this.userService.findByEmail(username));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthoritiesByRole(Role role) {
        List<Role> roles = roleService.getRolesLessThan(role);
        List<GrantedAuthority> auth = new ArrayList<>();
        for(Role elt : roles){
            auth.add(new SimpleGrantedAuthority(elt.getRoleName()));
        }
        return auth;
    }
}