package com.epsi.epsistore.services.Impls;

import com.epsi.core.entities.Role;
import com.epsi.core.repositories.RoleRepository;
import com.epsi.epsistore.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    public RoleRepository repository;

    @Override
    public List<Role> getRolesLessThan(Role role) {
        List<Role> roles = repository.findAll();
        List<Role> res = new ArrayList<>();
        for(Role elt : roles){
            if(elt.getPriorityIndex() < role.getPriorityIndex())
                res.add(elt);
        }
        res.add(role);
        return res;
    }
}
