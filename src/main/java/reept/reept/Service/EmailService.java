package reept.reept.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\nValid for 5 minutes.");
        mailSender.send(message);
    }
    
//    public void sendEmail(String to, String subject, String content) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("task.list@resustainability.com"); // use the same email as in application.properties
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(content);
//        mailSender.send(message);
//    }
    
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("task.list@resustainability.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // ✅ true = send HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // You can optionally log this or throw a runtime exception
        }
    
    
    }   
}