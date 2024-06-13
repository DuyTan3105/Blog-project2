package com.example.blog.services;

import com.example.blog.events.RegistrationCompleteEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
public class EmailService {

        private final JavaMailSender mailSender;
        private final RegistrationCompleteEvent event;

        public void sendSimpleMessage(String url) throws MessagingException, UnsupportedEncodingException {
            String subject = "Email Verification";
            String senderName = "User Registration Portal Service";
            String mailContent = "<p> Hi, "+ event.getUser().getName()+ ", </p>"+
                    "<p>Thank you for registering with us,"+"" +
                    "Please, follow the link below to complete your registration.</p>"+
                    "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                    "<p> Thank you <br> Users Registration Portal Service";
            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("tan31052003@gmail.com", senderName);
            messageHelper.setTo(event.getUser().getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);
            mailSender.send(message);

    }


}
