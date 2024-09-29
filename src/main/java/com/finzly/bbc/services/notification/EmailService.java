package com.finzly.bbc.services.notification;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String cachedTemplate;

    @PostConstruct
    public void init () {
        cachedTemplate = loadEmailTemplate ();
    }

    @Async("emailTaskExecutor")
    public void sendEmail (@Email String to, String subject, String body, boolean isHtml) {
        MimeMessage message = javaMailSender.createMimeMessage ();

        try {
            MimeMessageHelper helper = new MimeMessageHelper (message, true);
            helper.setTo (to);
            helper.setSubject (subject);
            helper.setText (body, isHtml);

            javaMailSender.send (message);
            log.info ("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error ("Failed to send email to {}: {}", to, e.getMessage (), e);
        } catch (Exception e) {
            log.error ("Unexpected error occurred while sending email to {}: {}", to, e.getMessage (), e);
        }
    }

    @Async("fileLoadingTaskExecutor")
    public void sendOtpEmail (String to, String otp) {
        String subject = "Verify Your Login";
        String body = cachedTemplate.replace ("{{otp}}", otp); // Replace OTP in the cached template

        sendEmail (to, subject, body, true);
    }

    private String loadEmailTemplate () {
        try {
            ClassPathResource resource = new ClassPathResource ("templates/otp_verification_email_template.html");
            return new String (FileCopyUtils.copyToByteArray (resource.getInputStream ()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error ("Error loading email template from classpath. Current classpath: {}", System.getProperty ("java.class.path"), e);
            return "";
        }
    }
}
