package com.epsi.epsistore.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterDTO {

    @JsonProperty(value = "email")
    private String username;
    @JsonProperty(value = "pwd")
    private String password;

}