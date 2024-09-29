package com.finzly.bbc.services.notification;

import com.finzly.bbc.exceptions.custom.otp.InvalidOtpException;
import com.finzly.bbc.exceptions.custom.otp.OtpAttemptLimitExceededException;
import com.finzly.bbc.exceptions.custom.otp.OtpExpiredException;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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

    public void sendOtpToUser (String userId) {
        if (userId.startsWith ("CST")) {
            handleOtpForCustomer (userId);
        } else if (userId.startsWith ("EMP")) {
            handleOtpForEmployee (userId);
        }
    }

    private void handleOtpForCustomer (String userId) {
        Optional<User> userOptional = userRepository.findByCustomer_CustomerId (userId);
        if (userOptional.isPresent ()) {
            Customer customer = userOptional.get ().getCustomer ();
            processOtp (customer.getUser ().getEmail (), customer.getOtp (), customer);
        }
    }

    private void handleOtpForEmployee (String userId) {
        Optional<User> userOptional = userRepository.findByEmployee_EmployeeId (userId);
        if (userOptional.isPresent ()) {
            Employee employee = userOptional.get ().getEmployee ();
            processOtp (employee.getUser ().getEmail (), employee.getOtp (), employee);
        }
    }

    private void processOtp (String email, OTP otp, Object entity) {
        LocalDateTime now = LocalDateTime.now ();
        if (otp == null) {
            // Generate and save a new OTP
            otp = new OTP ();
            otp.generateOtp ();
            saveOtpForEntity (otp, entity);
            emailService.sendOtpEmail (email, otp.getOtp ());
            return;
        }

        if (otp.isValidOtp () && !otp.isAttemptLimitExceeded ()) {
            otp.setOtp (OtpUtil.generateOtpCode (6));
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else if (otp.isExpired ()) {
            otp.generateOtp ();
            otp.setOtpAttemptCount (1);
            otp.setCreatedAt (now);
            otp.setExpiresAt (now.plusMinutes (5));
        } else if (otp.isAttemptLimitExceeded ()) {
            long minutesSinceCreated = Duration.between (otp.getCreatedAt (), now).toMinutes ();
            if (minutesSinceCreated < 60) {
                long minutesUntilRetry = 60 - minutesSinceCreated;
                throw new OtpAttemptLimitExceededException ("Limit exceeded. Please wait " + minutesUntilRetry + " minutes before trying to regenerate the OTP.");
            } else {
                otp.setOtpAttemptCount (1);
                otp.setCreatedAt (now);
                otp.setExpiresAt (now.plusMinutes (5));
            }
        }

        otpRepository.save (otp);
        saveOtpForEntity (otp, entity);
        emailService.sendOtpEmail (email, otp.getOtp ());
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


    public String verifyOtpForUser (String userId, String otpCode) {
        if (userId.startsWith ("CST")) {
          return   verifyOtpForCustomer (userId, otpCode);
        } else if (userId.startsWith ("EMP")) {
          return   verifyOtpForEmployee (userId, otpCode);
        }
        else
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
    }

    private String verifyOtpForEmployee (String userId, String otpCode) {
        Optional<Employee> employeeOptional = employeeRepository.findById (userId);
        if (employeeOptional.isPresent ()) {
            Employee employee = employeeOptional.get ();
           return  verifyOtp (employee.getOtp (), otpCode, employee);
        }
        else
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
    }

    private String verifyOtpForCustomer (String userId, String otpCode) {
        Optional<Customer> customerOptional = customerRepository.findById (userId);
        if (customerOptional.isPresent ()) {
            Customer customer = customerOptional.get ();
           return verifyOtp (customer.getOtp (), otpCode, customer);
        }
        else
            throw new IllegalArgumentException ("Invalid user ID: " + userId);
    }

    private String verifyOtp (OTP otp, String otpCode, Customer customer) {
        if (!otp.isValidOtp ()) {
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isExpired ()) {
            throw new OtpExpiredException ("OTP expired. Please try again.");
        }
        if (!otpCode.equals (otp.getOtp ())) {
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otpRepository.save (otp);
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isAttemptLimitExceeded ()) {
            throw new OtpAttemptLimitExceededException ("Limit exceeded. Please try again later.");
        }
        if (otp.isValidOtp ()) {
            customer.setOtp (null);
            customerRepository.save (customer);
            otpRepository.delete (otp);

            return jwtUtil.generateToken (customer.getCustomerId (), null, "CUSTOMER");
        }

        return "";
    }

    private String verifyOtp (OTP otp, String otpCode, Employee employee) {
        if (!otp.isValidOtp ()) {
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isExpired ()) {
            throw new OtpExpiredException ("OTP expired. Please try again.");
        }
        if (!otpCode.equals (otp.getOtp ())) {
            otp.setOtpAttemptCount (otp.getOtpAttemptCount () + 1);
            otpRepository.save (otp);
            throw new InvalidOtpException ("Invalid OTP. Please try again.");
        }
        if (otp.isAttemptLimitExceeded ()) {
            throw new OtpAttemptLimitExceededException ("Limit exceeded. Please try again later.");
        }
        if (otp.isValidOtp ()) {
            employee.setOtp (null);
            employeeRepository.save (employee);
            otpRepository.delete (otp);

            return jwtUtil.generateToken (employee.getEmployeeId (), null, "EMPLOYEE");
        }

        return "";
    }





}
