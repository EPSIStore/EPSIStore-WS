package com.epsi.epsistore.entities;

import java.util.HashSet;

import org.hibernate.mapping.Set;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    @Column(name = "email")
    private String email;

    @Column(name = "pwd")
    private String pwd;

    @ManyToOne
    @JoinColumn(name = "name_role")
    private Role role;

}
