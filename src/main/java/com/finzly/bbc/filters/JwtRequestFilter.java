package com.finzly.bbc.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finzly.bbc.exceptions.UnauthorizedException;
import com.finzly.bbc.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper ();
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal (HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader ("Authorization");

        String userId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith ("Bearer ")) {
            jwt = authorizationHeader.substring (7);
            try {
                Map<String, Object> claims = jwtUtil.getAllClaims (jwt);
                userId = (String) claims.get ("userId");
                if (userId == null) {
                    throw new UnauthorizedException ("User ID not found in token");
                }
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException ("Token expired");
            } catch (Exception e) {
                throw new UnauthorizedException ("Unauthorized: " + e.getMessage ());
            }
        }

        if (userId != null && SecurityContextHolder.getContext ().getAuthentication () == null) {
            if (jwtUtil.validateToken (jwt, userId)) {
                String role = jwtUtil.getRole (jwt);
                UserDetails userDetails = new org.springframework.security.core.userdetails.User (userId, "",
                        List.of (new SimpleGrantedAuthority ("ROLE_" + role)));  // Add roles with "ROLE_" prefix
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken (userDetails, null, userDetails.getAuthorities ());
                authentication.setDetails (new WebAuthenticationDetailsSource ().buildDetails (request));
                SecurityContextHolder.getContext ().setAuthentication (authentication);
            }
        }

        filterChain.doFilter (request, response);
    }
}
