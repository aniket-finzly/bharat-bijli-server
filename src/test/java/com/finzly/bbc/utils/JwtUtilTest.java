package com.finzly.bbc.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp () {
        System.setProperty ("jwt.secret", "a9996e017fde8751e4aaf0c66f65d97b21188c6c07edd7ea9c61efee281baa684d17de4a05afcfca2118d8d396adf64168dca2fa550435bc107009f4c922712771c74dce25f79b4b8d3c32ce4f6120ed0cad48c16dd864b346570fb2f59a46e425cc955005a996c2735c3d6aa57fb8882e7a9e66079e055303b658a1b9040a7b1f835828ce77418116543212eb67069d181096a26fac900e6c24f879a530ad72f719f770ea2347d68f91a56cc46cc6363246c6471f6031be8497c1698501ab708f5ed4d19839eb32a66e5c0a0f3a2a8a1235b60748dfabb88c31a3d2979ab58287168a1f3c087976793c3673ab77454ca9094c938faccb83711a1940dec65a16");
        System.setProperty ("jwt.expiresIn", "86400");
    }

    @Test
    void testGenerateToken () {
        String token = jwtUtil.generateToken ("EMP123", null, "ADMIN");
        System.out.println ("Generated Token: " + token);
        assertNotNull (token);
    }

    @Test
    void testGetEmployeeId () {
        String token = jwtUtil.generateToken ("EMP123", null, "ADMIN");
        String employeeId = jwtUtil.getEmployeeId (token);
        assertEquals ("EMP123", employeeId);
    }

    @Test
    void testGetCustomerId () {
        String token = jwtUtil.generateToken (null, "CST123", "CUSTOMER");
        System.out.println ("Token for Customer: " + token);
        String customerId = jwtUtil.getCustomerId (token);
        System.out.println ("Extracted Customer ID: " + customerId);
        assertEquals ("CST123", customerId);
    }

    @Test
    void testValidateToken () {
        String token = jwtUtil.generateToken ("EMP123", null, "ADMIN");
        System.out.println ("Token for Validation: " + token);
        boolean isValid = jwtUtil.validateToken (token, "EMP123");
        System.out.println ("Is Valid Token for EMP123: " + isValid);
        assertTrue (isValid);

        boolean isInvalid = jwtUtil.validateToken (token, "INVALID_ID");
        System.out.println ("Is Valid Token for INVALID_ID: " + isInvalid);
        assertFalse (isInvalid);
    }

    @Test
    void testGetExpiration () {
        String token = jwtUtil.generateToken ("EMP123", null, "ADMIN");
        System.out.println ("Token for Expiration Test: " + token);
        Date expiration = jwtUtil.getExpiration (token);
        System.out.println ("Token Expiration Time: " + expiration);
        assertNotNull (expiration);
        assertTrue (expiration.after (new Date ()));
    }
}
