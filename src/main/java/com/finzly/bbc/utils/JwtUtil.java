package com.finzly.bbc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiresIn}")
    private long expiresIn;

    private SecretKey getSigningKey () {
        return Keys.hmacShaKeyFor (secretKey.getBytes ());
    }

    public String getEmployeeId (String token) {
        Claims claims = getAllClaims (token);
        return claims.getSubject ();
    }

    public String getCustomerId (String token) {
        Claims claims = getAllClaims (token);
        return claims.get ("custId", String.class);
    }

    public Date getExpiration (String token) {
        return getAllClaims (token).getExpiration ();
    }

    private Claims getAllClaims (String token) {
        return Jwts.parser ()
                .verifyWith (getSigningKey ())
                .build ()
                .parseSignedClaims (token)
                .getPayload ();
    }

    Boolean isTokenExpired (String token) {
        return getExpiration (token).before (new Date ());
    }

    public String generateToken (String empId, String custId, String role) {
        Map<String, Object> claims = new HashMap<> ();
        claims.put ("role", role);
        return createToken (claims, empId != null ? empId : custId);
    }

    private String createToken (Map<String, Object> claims, String subject) {
        return Jwts.builder ()
                .claims (claims)
                .subject (subject)
                .issuedAt (new Date (System.currentTimeMillis ()))
                .expiration (new Date (System.currentTimeMillis () + 1000 * expiresIn))
                .signWith (getSigningKey ())
                .compact ();
    }

    public Boolean validateToken (String token, String userId) {
        final String extractedId = getEmployeeId (token);
        return (extractedId.equals (userId) && !isTokenExpired (token));
    }
}
