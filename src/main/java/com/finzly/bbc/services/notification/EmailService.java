package com.finzly.bbc.services.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendEmail (@Email String to, String subject, String body, boolean isHtml) {
        MimeMessage message = javaMailSender.createMimeMessage ();

        try {
            MimeMessageHelper helper = new MimeMessageHelper (message, true);
            helper.setTo (to);
            helper.setSubject (subject);
            helper.setText (body, isHtml);

            javaMailSender.send (message);
            log.info ("Email sent successfully to: {}", to);
            return CompletableFuture.completedFuture (null);
        } catch (MessagingException e) {
            log.error ("Failed to send email to {}: {}", to, e.getMessage (), e);
            return CompletableFuture.failedFuture (e);
        } catch (Exception e) {
            log.error ("Unexpected error occurred while sending email to {}: {}", to, e.getMessage (), e);
            return CompletableFuture.failedFuture (e);
        }
    }

    @Async("fileLoadingTaskExecutor")
    public void sendOtpEmail (String to, String otp) {
        String subject = "Verify Your Login";
        log.info ("Sending OTP email to: {} otp {}", to, otp);

        String body = getCachedTemplate ().replace ("{{otp}}", otp); // Use cached template

        // Send email without waiting for the result
        sendEmail (to, subject, body, true).exceptionally (ex -> {
            log.error ("Error occurred while sending OTP email to {}: {}", to, ex.getMessage ());
            return null; // Return null as we're not interested in the result
        });
    }

    @Cacheable("emailTemplateCache")
    private String getCachedTemplate () {
        try {
            ClassPathResource resource = new ClassPathResource ("templates/otp_verification_email_template.html");
            return new String (FileCopyUtils.copyToByteArray (resource.getInputStream ()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error ("Error loading email template from classpath. Current classpath: {}", System.getProperty ("java.class.path"), e);
            return "";
        }
    }
}
