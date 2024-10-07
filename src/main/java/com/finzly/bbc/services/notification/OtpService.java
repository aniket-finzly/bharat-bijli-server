package com.finzly.bbc.services.notification;

import com.finzly.bbc.exceptions.*;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.models.notification.OTP;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.auth.EmployeeRepository;
import com.finzly.bbc.repositories.auth.UserRepository;
import com.finzly.bbc.repositories.notification.OTPRepository;
import com.finzly.bbc.utils.JwtUtil;
import com.finzly.bbc.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    // Main method to generate OTP based on user type
    public void generateOtp (String userId) {
        Object entity = getEntityByUserId (userId);
        generateOtpForEntity (entity, userId);
    }

    // Main method to resend OTP based on user type
    public void resendOtp (String userId) {
        Object entity = getEntityByUserId (userId);
        resendOrGenerateOtp (entity);
    }

    // Main method to verify OTP based on user type
    public String verifyOtp (String userId, String otpCode) {
        Object entity = getEntityByUserId (userId);
        OTP otp = getOtpFromEntity (entity);
        return verifyOtpCommon (otp, otpCode, entity, userId);
    }

    // Retrieve the entity based on userId prefix (USR, CST, EMP)
    private Object getEntityByUserId (String userId) {
        if (userId.startsWith ("USR")) {
            return userRepository.findById (userId)
                    .orElseThrow (() -> new ResourceNotFoundException ("User not found with ID: " + userId));
        } else if (userId.startsWith ("CST")) {
            return customerRepository.findById (userId)
                    .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + userId));
        } else if (userId.startsWith ("EMP")) {
            return employeeRepository.findById (userId)
                    .orElseThrow (() -> new ResourceNotFoundException ("Employee not found with ID: " + userId));
        } else {
            throw new BadRequestException ("Invalid user ID");
        }
    }

    // Generate OTP for the entity (User, Customer, Employee)
    private void generateOtpForEntity (Object entity, String userId) {
        OTP otp = getOtpFromEntity (entity);

        if (otp != null && !otp.isExpired () && !otp.isAttemptLimitExceeded ()) {
            throw new ConflictException ("An OTP is already valid for user: " + userId);
        } else if (otp != null && otp.isExpired ()) {
            otp.setOtpAttemptCount (0);
            otpRepository.save (otp);
        } else if (otp != null && otp.isAttemptLimitExceeded ()) {
            throw new TooManyRequestsException ("OTP attempt limit exceeded for user: " + userId);
        }

        // Generate new OTP
        otp = new OTP ();
        otp.generateOtp ();
        saveOtpForEntity (otp, entity);
        sendOtpEmail (getEmailFromEntity (entity), otp.getOtp ());
    }

    // Resend or generate a new OTP
    private void resendOrGenerateOtp (Object entity) {
        OTP otp = getOtpFromEntity (entity);
        LocalDateTime now = LocalDateTime.now ();

        if (otp == null || otp.isExpired ()) {
            // Generate a new OTP
            otp = new OTP ();
            otp.generateOtp ();
            otp.setOtpAttemptCount (1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else if (!otp.isAttemptLimitExceeded ()) {
            // Resend OTP
            otp.setOtp (OtpUtil.generateOtpCode (6));
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else {
            throw new TooManyRequestsException ("OTP attempt limit exceeded for user: " + getEmailFromEntity (entity));
        }

        otpRepository.save (otp);
        saveOtpForEntity (otp, entity);
        sendOtpEmail (getEmailFromEntity (entity), otp.getOtp ());
    }

    // Verify OTP (common for all entity types)
    private String verifyOtpCommon (OTP otp, String otpCode, Object entity, String userId) {
        validateOtp (otp, otpCode, userId);
        clearOtpForEntity (entity);
        otpRepository.delete (otp);
        return jwtUtil.generateToken (entity);
    }

    // Validate OTP details
    private void validateOtp (OTP otp, String otpCode, String userId) {
        if (otp == null) {
            throw new BadRequestException ("OTP does not exist.");
        }
        if (!otp.isValidOtp ()) {
            throw new BadRequestException ("Invalid OTP. Please try again.");
        }
        if (otp.isExpired ()) {
            throw new BadRequestException ("OTP expired. Please try again.");
        }
        if (!otpCode.equals (otp.getOtp ())) {
            handleInvalidOtpAttempt (otp, userId);
        }
        if (otp.isAttemptLimitExceeded ()) {
            throw new TooManyRequestsException ("OTP attempt limit exceeded for user: " + userId);
        }
    }

    // Handle invalid OTP attempts
    private void handleInvalidOtpAttempt (OTP otp, String userId) {
        otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
        otpRepository.save (otp);
        throw new BadRequestException ("Invalid OTP for user: " + userId + ". Please try again.");
    }

    // Send OTP via email
    private void sendOtpEmail (String email, String otpCode) {
        try {
            emailService.sendOtpEmail (email, otpCode);
        } catch (Exception e) {
            throw new InternalServerErrorException ("Failed to send OTP email to " + email);
        }
    }

    // Save OTP for the entity (User, Customer, Employee)
    private void saveOtpForEntity (OTP otp, Object entity) {
        if (entity instanceof User) {
            ((User) entity).setOtp (otp);
            userRepository.save ((User) entity);
        } else if (entity instanceof Customer) {
            ((Customer) entity).setOtp (otp);
            customerRepository.save ((Customer) entity);
        } else if (entity instanceof Employee) {
            ((Employee) entity).setOtp (otp);
            employeeRepository.save ((Employee) entity);
        }
    }

    // Clear OTP after verification
    private void clearOtpForEntity (Object entity) {
        if (entity instanceof User) {
            ((User) entity).setOtp (null);
            userRepository.save ((User) entity);
        } else if (entity instanceof Customer) {
            ((Customer) entity).setOtp (null);
            customerRepository.save ((Customer) entity);
        } else if (entity instanceof Employee) {
            ((Employee) entity).setOtp (null);
            employeeRepository.save ((Employee) entity);
        }
    }

    // Helper method to get the OTP from an entity (User, Customer, Employee)
    private OTP getOtpFromEntity (Object entity) {
        if (entity instanceof User) {
            return ((User) entity).getOtp ();
        } else if (entity instanceof Customer) {
            return ((Customer) entity).getOtp ();
        } else if (entity instanceof Employee) {
            return ((Employee) entity).getOtp ();
        }
        return null;
    }

    // Helper method to get the email from an entity (User, Customer, Employee)
    private String getEmailFromEntity (Object entity) {
        if (entity instanceof User) {
            return ((User) entity).getEmail ();
        } else if (entity instanceof Customer) {
            return ((Customer) entity).getUser ().getEmail ();
        } else if (entity instanceof Employee) {
            return ((Employee) entity).getUser ().getEmail ();
        }
        throw new BadRequestException ("Email not found for the given entity.");
    }
}
