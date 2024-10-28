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

    public void commonEmail(ToEmail toEmail) {
        // Creating a simple mail message
        SimpleMailMessage message = new SimpleMailMessage();
        // Who sent
        message.setFrom(from);
        // Who will receive
        message.setTo(toEmail.getTos());
        // email subject
        message.setSubject(toEmail.getSubject());
        // email content
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new BadRequestException(400,e.getMessage());
        }
    }

    public void htmlEmail(ToEmail toEmail) throws MessagingException {
        //create a MINE message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        // who sent
        minehelper.setFrom(from);
        // who will receive
        minehelper.setTo(toEmail.getTos());
        // email subject
        minehelper.setSubject(toEmail.getSubject());
        // email content  true indicates attachment or html
        minehelper.setText(toEmail.getContent(), true);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

}
