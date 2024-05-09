package com.epsi.epsistore.controllers;

import com.epsi.core.entities.User;
import com.epsi.epsistore.dtos.LoginDTO;
import com.epsi.epsistore.dtos.RegisterDTO;
import com.epsi.epsistore.dtos.ResponseBodyDTO;
import com.epsi.epsistore.dtos.TEST;
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
            System.out.println(loginDTO.getUsername() + "|" + loginDTO.getPassword());
            System.out.println(userDetailsService.loadUserByUsername(loginDTO.getUsername()).getPassword());
            System.out.println(loginDTO.getUsername() + "|" + loginDTO.getPassword());
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

    @GetMapping("/test")
    public String test(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        DeferredSecurityContext context = securityContextRepository.loadDeferredContext(request);
        if(context.isGenerated()){
            System.out.println("gene");
            System.out.println(context.get().getAuthentication());
        }
        else {
            System.out.println("No gene");
            System.out.println(context.get().getAuthentication());
        }
        if(authentication != null){
            System.out.println(authentication.getName());
        }
        if(session != null){
            return session.getId();
        }
        else{
            return "ok";
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        try {
            String response = userService.register(registerDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de l'inscription de l'utilisateur." + e.getMessage());
        }
    }

    @GetMapping("/get_cookie")
    public String getCookie(HttpServletRequest request){
        DeferredSecurityContext context = securityContextRepository.loadDeferredContext(request);
        if(context.isGenerated()){
            return "gene";
        }
        else{
            return "nongene";
        }
    }

    @GetMapping("/set_cookie")
    public String setCookie(@RequestParam("cookie") String id, @RequestParam("redirect_uri") String uri, HttpServletResponse response) throws IOException {
        response.addHeader("Set-Cookie", "JSESSIONID="+id+"; Path=/; HttpOnly; SameSite=None");
        response.sendRedirect(uri);
        return id;
    }
}
