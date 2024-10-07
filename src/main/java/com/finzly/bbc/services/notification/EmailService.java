package com.finzly.bbc.services.notification;

import com.finzly.bbc.dtos.billing.InvoiceResponse;
import com.finzly.bbc.utils.PdfGenerationUtility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PdfGenerationUtility pdfGenerationUtility;

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

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendInvoiceEmail (InvoiceResponse invoice) {
        try {
            // Generate PDF
            byte[] pdfBytes = pdfGenerationUtility.generateInvoicePdf (invoice);

            // Create email message
            MimeMessage message = javaMailSender.createMimeMessage ();
            MimeMessageHelper helper = new MimeMessageHelper (message, true);

            helper.setTo (invoice.getCustomerEmail ());
            String subject = String.format ("Electricity Bill - %s",
                    invoice.getMonth ().format (DateTimeFormatter.ofPattern ("MMMM yyyy")));

            String emailBody = createEmailBody (invoice);

            helper.setSubject (subject);
            helper.setText (emailBody, false);

            String fileName = String.format ("electricity_bill_%s.pdf",
                    invoice.getMonth ().format (DateTimeFormatter.ofPattern ("MMM_yyyy")));
            helper.addAttachment (fileName, new ByteArrayResource (pdfBytes));

            javaMailSender.send (message);
            log.info ("Invoice email sent successfully to: {}", invoice.getCustomerEmail ());

            return CompletableFuture.completedFuture (null);
        } catch (Exception e) {
            log.error ("Failed to send invoice email to {}: {}", invoice.getCustomerEmail (), e.getMessage (), e);
            return CompletableFuture.failedFuture (e);
        }
    }

    private String createEmailBody (InvoiceResponse invoice) {
        return String.format ("""
                        Dear %s,
                        
                        Your electricity bill for %s is attached to this email.
                        
                        Bill Summary:
                        - Bill Amount: ₹%.2f
                        - Due Date: %s
                        - Units Consumed: %d
                        
                        Please ensure payment by the due date to avoid any service interruption.
                        
                        If you have any questions about your bill, please contact our customer support.
                        
                        Thank you for your business.
                        
                        Best regards,
                        Finzly BBC Team
                        """,
                invoice.getCustomerName (),
                invoice.getMonth ().format (DateTimeFormatter.ofPattern ("MMMM yyyy")),
                invoice.getFinalAmount (),
                invoice.getDueDate ().format (DateTimeFormatter.ofPattern ("dd MMMM yyyy")),
                invoice.getUnits ()
        );
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
