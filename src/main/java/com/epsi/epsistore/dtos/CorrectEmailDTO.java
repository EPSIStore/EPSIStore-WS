package com.epsi.epsistore.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorrectEmailDTO {

    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "userId")
    private int userId;
}
