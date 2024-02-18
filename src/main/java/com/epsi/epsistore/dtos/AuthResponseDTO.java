package com.epsi.epsistore.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@Setter
public class AuthResponseDTO {
    @JsonProperty("token")
    private String token;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private Collection<? extends GrantedAuthority> roles;

    public AuthResponseDTO(String token, String username, Collection<? extends GrantedAuthority> roles) {
        this.token = token;
        this.email = username;
        this.roles = roles;
    }
}