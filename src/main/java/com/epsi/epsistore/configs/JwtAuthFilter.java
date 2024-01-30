package com.epsi.epsistore.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epsi.epsistore.services.impls.UserDetailsServiceImpl;

import java.io.IOException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

// method to filter and check the presence of the token in the request header
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // get the "Authorization" header
        final String authHeader = request.getHeader(AUTHORIZATION);

        final String userEmail;
        final String jwtToken;

        // condition that checks if the "Authorization" header is empty or does not start with "Bearer"
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        // allows you to extract the JWT token from the header by removing the "Bearer" part
        jwtToken = authHeader.substring(7);
        // extract user email from JWT token using jwtUtils
        userEmail = jwtUtils.extractUserName(jwtToken);

        // check if the email is not null and if there is not already an authentication in progress
        if(userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null){
            // load user details from his email
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            // check if the token is valid
            if(jwtUtils.isTokenValid(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                // defines authentication details
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // defines user authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            // if response validated, move on to the next filter
            filterChain.doFilter(request, response );
        }
    }
}
