package com.example.demo.service;

import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.email.EmailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService implements EmailSender{

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;


    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    @Override
    //so we dont block the client
    @Async
    public void send(String to, String email) {
        try{
            /*This class represents a MIME style email message. It implements the Message abstract class and the MimePart interface.

Clients wanting to create new MIME style messages will instantiate an empty MimeMessage object and then fill it with appropriate attributes and content.*/
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            /*Helper class for populating a jakarta.mail.internet.MimeMessage.

Mirrors the simple setters of org.springframework.mail.SimpleMailMessage, directly applying the values to the underlying MimeMessage. Allows for defining a character encoding for the entire message, automatically applied by all methods of this helper class.

*/
            //makes the email
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("confirm your email");
            helper.setFrom("PokedexRegister@aol.com");
            //sends the email
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            LOGGER.error("failed to send email",e);
            throw new IllegalStateException("failed to send email");
        }
        
    }



    
}
