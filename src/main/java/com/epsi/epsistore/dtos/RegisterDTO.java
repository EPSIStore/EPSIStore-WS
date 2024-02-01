package com.epsi.epsistore.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterDTO {

    @JsonProperty("email")
    private String username;
    @JsonProperty("pwd")
    private String pwd;

}
