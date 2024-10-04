package com.finzly.bbc.utils;

import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiresIn}")
    private long expiresIn;

    private SecretKey getSigningKey () {
        try {
            return Keys.hmacShaKeyFor (secretKey.getBytes ());
        } catch (Exception e) {
            log.error ("Error generating signing key: {}", e.getMessage ());
            throw new IllegalArgumentException ("Invalid secret key configuration.");
        }
    }

    public String generateToken (Object user) {
        Map<String, Object> claims;
        claims = new HashMap<> (getEntityFieldsAsMap (user));
        return createToken (claims, (String) claims.get ("userId"));
    }

    private String createToken (Map<String, Object> claims, String subject) {
        try {
            return Jwts.builder ().claims (claims).subject (subject).issuedAt (new Date (System.currentTimeMillis ())).expiration (new Date (System.currentTimeMillis () + 1000 * expiresIn))
                    .signWith (getSigningKey ())
                    .compact ();
        } catch (Exception e) {
            log.error ("Error creating token: {}", e.getMessage ());
            throw new IllegalArgumentException ("Token creation failed.");
        }
    }

    private Map<String, Object> getEntityFieldsAsMap (Object entity) {
        Map<String, Object> fieldsMap = new HashMap<> ();

        switch (entity) {
            case Employee employee -> {
                fieldsMap.put ("firstName", employee.getUser ().getFirstName ());
                fieldsMap.put ("lastName", employee.getUser ().getLastName ());
                fieldsMap.put ("email", employee.getUser ().getEmail ());
                fieldsMap.put ("phoneNumber", employee.getUser ().getPhoneNumber ());
                fieldsMap.put ("designation", employee.getDesignation ());
                fieldsMap.put ("salary", employee.getSalary ());
                fieldsMap.put ("userId", employee.getEmployeeId ());
                fieldsMap.put ("role", "EMPLOYEE");
            }
            case Customer customer -> {
                fieldsMap.put ("firstName", customer.getUser ().getFirstName ());
                fieldsMap.put ("lastName", customer.getUser ().getLastName ());
                fieldsMap.put ("email", customer.getUser ().getEmail ());
                fieldsMap.put ("phoneNumber", customer.getUser ().getPhoneNumber ());
                fieldsMap.put ("userId", customer.getCustomerId ());
                fieldsMap.put ("role", "CUSTOMER");
            }
            case User user -> {
                fieldsMap.put ("firstName", user.getFirstName ());
                fieldsMap.put ("lastName", user.getLastName ());
                fieldsMap.put ("email", user.getEmail ());
                fieldsMap.put ("phoneNumber", user.getPhoneNumber ());
                fieldsMap.put ("userId", user.getId ());

                if (user.isAdmin ()) {
                    fieldsMap.put ("role", "ADMIN");
                } else {
                    fieldsMap.put ("role", "CUSTOMER");
                }
            }
            default -> {
                log.warn ("Unknown entity type: {}", entity.getClass ().getName ());
            }
        }
        return fieldsMap;
    }

    public Date getExpiration (String token) {
        try {
            Claims claims = getAllClaims (token);
            return claims.getExpiration ();
        } catch (Exception e) {
            log.error ("Error extracting expiration from token: {}", e.getMessage ());
            throw new IllegalArgumentException ("Invalid token.");
        }
    }

    private boolean isTokenExpired (String token) {
        Date expiration = getExpiration (token);
        return expiration.before (new Date ());
    }

    public boolean validateToken (String token, String userId) {
        try {
            final String tokenUserId = getAllClaims (token).get ("userId").toString ();
            return (tokenUserId.equals (userId) && !isTokenExpired (token));
        } catch (SignatureException e) {
            log.error ("Invalid JWT signature: {}", e.getMessage ());
            return false;
        } catch (Exception e) {
            log.error ("Token validation failed: {}", e.getMessage ());
            return false;
        }
    }

    public String getRole (String token) {
        try {
            Claims claims = getAllClaims (token);
            return claims.get ("role", String.class);
        } catch (Exception e) {
            log.error ("Error retrieving role from token: {}", e.getMessage ());
            throw new IllegalArgumentException ("Invalid token.");
        }
    }

    public Claims getAllClaims (String token) {
        try {
            return Jwts.parser ().verifyWith (getSigningKey ()).build ().parseSignedClaims (token).getPayload ();
        } catch (Exception e) {
            log.error ("Error parsing token: {}", e.getMessage ());
            throw new IllegalArgumentException ("Invalid token.");
        }
    }
}
