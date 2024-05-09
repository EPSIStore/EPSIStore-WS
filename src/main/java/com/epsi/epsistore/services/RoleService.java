package com.epsi.epsistore.services;

import com.epsi.core.entities.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public List<Role> getRolesLessThan(Role role);
}
