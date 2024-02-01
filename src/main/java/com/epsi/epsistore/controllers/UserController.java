package com.epsi.epsistore.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.epsi.epsistore.configs.JwtUtils;
import com.epsi.epsistore.dtos.AuthResponseDTO;
import com.epsi.epsistore.dtos.LoginDTO;
import com.epsi.epsistore.dtos.RegisterDTO;
import com.epsi.epsistore.services.UserService;

import org.slf4j.Logger;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;


    // api request for connection
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
        }catch (Exception e){
            LOGGER.info("not through");
            return ResponseEntity.status(400).body(e.getMessage());
        }

        LOGGER.info("through");
        final UserDetails user = userDetailsService.loadUserByUsername(loginDto.getUsername());

        if(user != null){
            return ResponseEntity.ok(new AuthResponseDTO(jwtUtils.generateToken(user),user.getUsername(), user.getAuthorities()));
        }
        return ResponseEntity.status(400).body("Error occured");
    }


    // api request for register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        try {
            AuthResponseDTO authResponseDto = userService.register(registerDto);
            return ResponseEntity.ok(authResponseDto);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenue lors de l'inscription de l'utilisateur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de l'inscription de l'utilisateur.");
        }
    }

}