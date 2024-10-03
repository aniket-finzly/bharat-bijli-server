package com.finzly.bbc.services.notification;

import com.finzly.bbc.exceptions.custom.otp.InvalidOtpException;
import com.finzly.bbc.exceptions.custom.otp.OtpAttemptLimitExceededException;
import com.finzly.bbc.exceptions.custom.otp.OtpExpiredException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.notification.OTP;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.auth.EmployeeRepository;
import com.finzly.bbc.repositories.notification.OTPRepository;
import com.finzly.bbc.utils.JwtUtil;
import com.finzly.bbc.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

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

    /**
     * Generate a new OTP for a user
     */
    public void generateOtp (String userId) {
        if (userId.startsWith ("CST")) {
            generateOtpForCustomer (userId);
        } else if (userId.startsWith ("EMP")) {
            generateOtpForEmployee (userId);
        } else {
            log.error ("Invalid user ID prefix: {}", userId);
            throw new IllegalArgumentException ("Invalid user ID prefix: " + userId);
        }
    }

    private void generateOtpForCustomer (String userId) {
        Optional<Customer> customerOptional = customerRepository.findById (userId);
        if (customerOptional.isPresent ()) {
            Customer customer = customerOptional.get ();
            OTP otp = customer.getOtp ();

            // Check if an existing OTP is valid
            if (otp != null) {
                if (!otp.isExpired () && !otp.isAttemptLimitExceeded ()) {
                    log.warn ("An OTP is already valid for customer: {}", userId);
                    throw new IllegalStateException ("An OTP is already valid. Please wait for it to expire or try resending it.");
                } else if (otp.isExpired ()) {
                    otp.setOtpAttemptCount (0);
                    otpRepository.save (otp);
                } else if (otp.isAttemptLimitExceeded ()) {
                    log.warn ("OTP attempt limit exceeded for customer: {}", userId);
                    throw new OtpAttemptLimitExceededException ("Limit exceeded. Please wait before trying to regenerate the OTP.");
                }
            }

            // Generate a new OTP
            otp = new OTP ();
            otp.generateOtp ();
            saveOtpForEntity (otp, customer);
            sendOtpEmail (customer.getUser ().getEmail (), otp.getOtp ());
            log.info ("OTP generated for customer: {}", userId);
        } else {
            log.error ("Customer not found for userId: {}", userId);
            throw new IllegalArgumentException ("Customer not found for userId: " + userId);
        }
    }

    private void generateOtpForEmployee (String userId) {
        Optional<Employee> employeeOptional = employeeRepository.findById (userId);
        if (employeeOptional.isPresent ()) {
            Employee employee = employeeOptional.get ();
            OTP otp = employee.getOtp ();

            // Check if an existing OTP is valid
            if (otp != null) {
                if (!otp.isExpired () && !otp.isAttemptLimitExceeded ()) {
                    log.warn ("An OTP is already valid for employee: {}", userId);
                    throw new IllegalStateException ("An OTP is already valid. Please wait for it to expire or try resending it.");
                } else if (otp.isExpired ()) {
                    otp.setOtpAttemptCount (0);
                    otpRepository.save (otp);
                } else if (otp.isAttemptLimitExceeded ()) {
                    log.warn ("OTP attempt limit exceeded for employee: {}", userId);
                    throw new OtpAttemptLimitExceededException ("Limit exceeded. Please wait before trying to regenerate the OTP.");
                }
            }

            // Generate a new OTP
            otp = new OTP ();
            otp.generateOtp ();
            saveOtpForEntity (otp, employee);
            sendOtpEmail (employee.getUser ().getEmail (), otp.getOtp ());
            log.info ("OTP generated for employee: {}", userId);
        } else {
            log.error ("Employee not found for userId: {}", userId);
            throw new IllegalArgumentException ("Employee not found for userId: " + userId);
        }
    }


    /**
     * Resend an OTP for a user, either reuse if valid or regenerate if expired.
     */
    public void resendOtp (String userId) {
        if (userId.startsWith ("CST")) {
            resendOtpForCustomer (userId);
        } else if (userId.startsWith ("EMP")) {
            resendOtpForEmployee (userId);
        } else {
            log.error ("Invalid user ID prefix: {}", userId);
            throw new IllegalArgumentException ("Invalid user ID prefix: " + userId);
        }
    }

    private void resendOtpForCustomer (String userId) {
        Optional<Customer> customerOptional = customerRepository.findById (userId);
        if (customerOptional.isPresent ()) {
            Customer customer = customerOptional.get ();
            resendOrGenerateOtp (customer.getOtp (), customer.getUser ().getEmail (), customer);
        } else {
            log.error ("Customer not found for userId: {}", userId);
            throw new IllegalArgumentException ("Customer not found for userId: " + userId);
        }
    }

    private void resendOtpForEmployee (String userId) {
        Optional<Employee> employeeOptional = employeeRepository.findById (userId);
        if (employeeOptional.isPresent ()) {
            Employee employee = employeeOptional.get ();
            resendOrGenerateOtp (employee.getOtp (), employee.getUser ().getEmail (), employee);
        } else {
            log.error ("Employee not found for userId: {}", userId);
            throw new IllegalArgumentException ("Employee not found for userId: " + userId);
        }
    }

    private void resendOrGenerateOtp (OTP otp, String email, Object entity) {
        LocalDateTime now = LocalDateTime.now ();
        if (otp == null || otp.isExpired ()) {
            // Generate a new OTP if no OTP exists or it's expired
            otp = new OTP ();
            otp.generateOtp ();
            otp.setOtpAttemptCount (1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else if (!otp.isAttemptLimitExceeded ()) {
            otp.setOtp (OtpUtil.generateOtpCode (6));
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else {
            log.warn ("OTP attempt limit exceeded for email: {}", email);
            throw new OtpAttemptLimitExceededException ("Limit exceeded. Please wait before trying to regenerate the OTP.");
        }

        otpRepository.save (otp);
        saveOtpForEntity (otp, entity);
        sendOtpEmail (email, otp.getOtp ());
        log.info ("OTP resent to email: {}", email);
    }

    /**
     * Verify the provided OTP for a user
     */
    public String verifyOtp (String userId, String otpCode) {
        if (userId.startsWith ("CST")) {
            return verifyOtpForCustomer (userId, otpCode);
        } else if (userId.startsWith ("EMP")) {
            return verifyOtpForEmployee (userId, otpCode);
        } else {
            log.error ("Invalid user ID: {}", userId);
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
        }
    }

    private String verifyOtpForCustomer (String userId, String otpCode) {
        Optional<Customer> customerOptional = customerRepository.findById (userId);
        if (customerOptional.isPresent ()) {
            Customer customer = customerOptional.get ();
            return verifyOtpCommon (customer.getOtp (), otpCode, customer, "CUSTOMER");
        } else {
            log.error ("Customer not found for userId: {}", userId);
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
        }
    }

    private String verifyOtpForEmployee (String userId, String otpCode) {
        Optional<Employee> employeeOptional = employeeRepository.findById (userId);
        if (employeeOptional.isPresent ()) {
            Employee employee = employeeOptional.get ();
            return verifyOtpCommon (employee.getOtp (), otpCode, employee, "EMPLOYEE");
        } else {
            log.error ("Employee not found for userId: {}", userId);
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
        }
    }

    private String verifyOtpCommon (OTP otp, String otpCode, Object entity, String userType) {
        if (otp == null || !otp.isValidOtp ()) {
            log.warn ("Invalid OTP for user: {}", userType);
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isExpired ()) {
            log.warn ("OTP expired for user: {}", userType);
            throw new OtpExpiredException ("OTP expired. Please try again.");
        }
        if (!otpCode.equals (otp.getOtp ())) {
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otpRepository.save (otp);
            log.warn ("Invalid OTP entered for user: {}", userType);
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isAttemptLimitExceeded ()) {
            log.warn ("OTP attempt limit exceeded for user: {}", userType);
            throw new OtpAttemptLimitExceededException ("Limit exceeded. Please try again later.");
        }

        // OTP is valid
        if (entity instanceof Customer) {
            ((Customer) entity).setOtp (null);
            customerRepository.save ((Customer) entity);
        } else if (entity instanceof Employee) {
            ((Employee) entity).setOtp (null);
            employeeRepository.save ((Employee) entity);
        }

        otpRepository.delete (otp);
        return jwtUtil.generateToken (entity);
    }

    private void sendOtpEmail (String email, String otpCode) {
        try {
            emailService.sendOtpEmail (email, otpCode);
        } catch (Exception e) {
            log.error ("Failed to send OTP email to {}: {}", email, e.getMessage ());
            throw new RuntimeException ("Error sending OTP email. Please try again.");
        }
    }

    private void saveOtpForEntity (OTP otp, Object entity) {
        if (entity instanceof Customer) {
            ((Customer) entity).setOtp (otp);
            customerRepository.save ((Customer) entity);
        } else if (entity instanceof Employee) {
            ((Employee) entity).setOtp (otp);
            employeeRepository.save ((Employee) entity);
        }
    }
}
