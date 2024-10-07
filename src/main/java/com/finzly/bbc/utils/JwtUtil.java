package com.finzly.bbc.utils;

import com.finzly.bbc.constants.UserRole;
import com.finzly.bbc.exceptions.UnauthorizedException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.auth.EmployeeRepository;
import com.finzly.bbc.repositories.auth.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private SecretKey getSigningKey () {
        return Keys.hmacShaKeyFor (secretKey.getBytes ());
    }

    public String generateToken (Object user) {
        Map<String, Object> claims = getEntityFieldsAsMap (user);
        return createToken (claims, (String) claims.get ("userId"));
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

    private Map<String, Object> getEntityFieldsAsMap (Object entity) {
        Map<String, Object> fieldsMap = new HashMap<> ();

        if (entity instanceof Employee employee) {
            populateEmployeeFields (fieldsMap, employee);
        } else if (entity instanceof Customer customer) {
            populateCustomerFields (fieldsMap, customer);
        } else if (entity instanceof User user) {
            populateUserFields (fieldsMap, user);
        } else {
            log.warn ("Unknown entity type: {}", entity.getClass ().getName ());
        }
        return fieldsMap;
    }

    private void populateEmployeeFields (Map<String, Object> fieldsMap, Employee employee) {
        fieldsMap.put ("firstName", employee.getUser ().getFirstName ());
        fieldsMap.put ("lastName", employee.getUser ().getLastName ());
        fieldsMap.put ("email", employee.getUser ().getEmail ());
        fieldsMap.put ("phoneNumber", employee.getUser ().getPhoneNumber ());
        fieldsMap.put ("designation", employee.getDesignation ());
        fieldsMap.put ("salary", employee.getSalary ());
        fieldsMap.put ("userId", employee.getEmployeeId ());
        fieldsMap.put ("role", UserRole.EMPLOYEE);
    }

    private void populateCustomerFields (Map<String, Object> fieldsMap, Customer customer) {
        fieldsMap.put ("firstName", customer.getUser ().getFirstName ());
        fieldsMap.put ("lastName", customer.getUser ().getLastName ());
        fieldsMap.put ("email", customer.getUser ().getEmail ());
        fieldsMap.put ("phoneNumber", customer.getUser ().getPhoneNumber ());
        fieldsMap.put ("userId", customer.getCustomerId ());
        fieldsMap.put ("role", UserRole.CUSTOMER);
    }

    private void populateUserFields (Map<String, Object> fieldsMap, User user) {
        fieldsMap.put ("firstName", user.getFirstName ());
        fieldsMap.put ("lastName", user.getLastName ());
        fieldsMap.put ("email", user.getEmail ());
        fieldsMap.put ("phoneNumber", user.getPhoneNumber ());
        fieldsMap.put ("userId", user.getId ());
        fieldsMap.put ("role", user.isAdmin () ? UserRole.ADMIN : UserRole.CUSTOMER);
    }

    public Date getExpiration (String token) {
        try {
            Claims claims = getAllClaims (token);
            return claims.getExpiration ();
        } catch (Exception e) {
            log.error ("Error extracting expiration from token: {}", e.getMessage ());
            throw new UnauthorizedException ("Invalid token. Please log in again.");
        }
    }

    private boolean isTokenExpired (String token) {
        return getExpiration (token).before (new Date ());
    }

    public boolean validateToken (String token, String userId) {
        try {
            Claims claims = getAllClaims (token);
            String role = claims.get ("role", String.class);
            return isValidUser (role, userId) && !isTokenExpired (token);
        } catch (Exception e) {
            log.error ("Error validating token: {}", e.getMessage ());
            throw new UnauthorizedException ("Invalid token. Please log in again.");
        }
    }

    private boolean isValidUser (String role, String id) {
        return switch (role) {
            case UserRole.CUSTOMER -> customerRepository.existsById (id);
            case UserRole.EMPLOYEE -> employeeRepository.existsById (id);
            default -> userRepository.existsById (id);
        };
    }

    public String getRole (String token) {
        try {
            Claims claims = getAllClaims (token);
            return claims.get ("role", String.class);
        } catch (Exception e) {
            log.error ("Error retrieving role from token: {}", e.getMessage ());
            throw new UnauthorizedException ("Invalid token. Please log in again.");
        }
    }

    public Claims getAllClaims (String token) {
        try {
            return Jwts.parser ().verifyWith (getSigningKey ()).build ().parseSignedClaims (token).getPayload ();
        } catch (ExpiredJwtException e) {
            log.error ("Error parsing token: {}", e.getMessage ());
            throw new UnauthorizedException ("Invalid token. Please log in again.");
        }
    }
}
