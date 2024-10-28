package com.haste.yzx.common.utils;

import com.haste.yzx.common.domain.bo.ToEmail;
import com.haste.yzx.common.exception.BadRequestException;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component(value = "customMailSender")
public class MailSender {
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender mailSender;

    /**
     * Send a simple email
     */
    public void commonEmail(ToEmail toEmail) {
        // Create a simple email message
        SimpleMailMessage message = new SimpleMailMessage();
        // Sender
        message.setFrom(from);
        // Recipient
        message.setTo(toEmail.getTos());
        // Email subject
        message.setSubject(toEmail.getSubject());
        // Email content
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new BadRequestException(400, e.getMessage());
        }
    }

    /**
     * Send an HTML email
     */
    public void htmlEmail(ToEmail toEmail) throws MessagingException {
        // Create a MIME message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(message, true);
        // Sender
        mimeHelper.setFrom(from);
        // Recipient
        mimeHelper.setTo(toEmail.getTos());
        // Email subject
        mimeHelper.setSubject(toEmail.getSubject());
        // Email content, true indicates attachments or HTML content
        mimeHelper.setText(toEmail.getContent(), true);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

}