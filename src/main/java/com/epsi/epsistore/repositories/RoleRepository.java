package com.epsi.epsistore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.epsi.epsistore.entities.Role;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(@Param("name_role") String roleName);

}
