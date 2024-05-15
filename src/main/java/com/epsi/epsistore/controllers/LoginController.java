package com.epsi.epsistore.controllers;

import com.epsi.core.entities.User;
import com.epsi.epsistore.dtos.*;
import com.epsi.epsistore.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "https://127.0.0.1:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    /// Informations diverses sur la connection :
    /// Le code dans le try du login authentifie l'utilisateur et sauvegarde le context dans le securityContextRepository
    /// Pour recuperer les informations sur l'utilisateur connecté depuis sa requete faire :
    /// DeferredSecurityContext context = securityContextRepository.loadDeferredContext(request);
    /// le DeferredSecurityContext obtenu est un objet permettant de recuperer le context de la requete
    /// Si context.isGenerated() est vrai alors le context viens d'etre genere et ne contient pas d'authentification.
    /// Sinon, il contient l'authentification de l'utilisateur

    @PostMapping("/login")
    public ResponseEntity<ResponseBodyDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        }
        catch (Exception e){
            return ResponseEntity.status(401).body(new ResponseBodyDTO(e.getMessage()));
        }
        final UserDetails user = userDetailsService.loadUserByUsername(loginDTO.getUsername());

        if(user != null){
            return ResponseEntity.ok(new ResponseBodyDTO(user.getUsername()+this.passwordEncoder.encode(user.getPassword())+user.getAuthorities()));
        }
        return ResponseEntity.status(401).body(new ResponseBodyDTO("Error occured"));
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "ok";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        try {
            String response = userService.register(registerDto);
            return ResponseEntity.ok(new ResponseBodyDTO(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBodyDTO("Une erreur est survenue lors de l'inscription de l'utilisateur." + e.getMessage()));
        }
    }

    @GetMapping("/anon")
    public boolean anonSession(HttpServletResponse response, HttpServletRequest request){
        HttpSession session = request.getSession(true);
        if(session.isNew()){
            return true;
        }
        else{
            return false;
        }
    }

    @PostMapping("/correct-credentials")
    public boolean CorrectCredentials(@RequestBody LoginDTO login){
        try {
            User user = userService.findByEmail(login.getUsername());
            return passwordEncoder.matches(login.getPassword(), user.getPwd());
        }
        catch (Exception e){
            return false;
        }
    }

    @PostMapping("/encode-password")
    public String EncodePassword(@RequestBody String password){
        return passwordEncoder.encode(password);
    }
}
