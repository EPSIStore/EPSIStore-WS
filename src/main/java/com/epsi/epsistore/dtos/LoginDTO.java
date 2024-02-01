package com.epsi.epsistore.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginDTO {

    @JsonProperty(value = "email")
    public String username;
    @JsonProperty(value = "pwd")
    public String password;

    public LoginDTO(String email, String password) {
        this.username = email;
        this.password = password;
    }
}