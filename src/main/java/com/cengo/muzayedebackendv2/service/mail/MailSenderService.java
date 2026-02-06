package com.cengo.muzayedebackendv2.service.mail;

import com.cengo.muzayedebackendv2.domain.dto.SendEmailDTO;
import com.cengo.muzayedebackendv2.exception.MailNotSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Validated
public class MailSenderService {
    @Value("${spring.mail.from}")
    private String mailFrom;

    @Value("${spring.mail.from.name}")
    private String mailFromName;

    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(MailSenderService.class);


    public void sendEmail(@Valid SendEmailDTO request) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        try{
            messageHelper.setFrom(mailFrom, mailFromName);
            messageHelper.setTo(request.email());
            messageHelper.setSubject(request.subject());
            messageHelper.setText(request.htmlBody(), true);
        }catch (MessagingException | UnsupportedEncodingException e){
            logger.error(e.toString());
            throw new MailNotSendException();
        }

        mailSender.send(message);
    }
}
