package com.cengo.muzayedebackendv2.service.mail;

import com.cengo.muzayedebackendv2.domain.dto.SendBidInfoDTO;
import com.cengo.muzayedebackendv2.domain.dto.SendEmailDTO;
import com.cengo.muzayedebackendv2.domain.dto.SendVerificationDTO;
import com.cengo.muzayedebackendv2.domain.enums.MailTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class MailTemplateService {
    @Value("${spring.mail.access.url}")
    private String baseUrl;

    @Value("${spring.mail.logo.url}")
    private String logoUrl;

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final MailSenderService mailSenderService;

    @Async
    public void sendBidInfoMail(@Valid SendBidInfoDTO request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("NEW_PRICE", request.price());
        variables.put("LOT", request.lotName());
        variables.put("NAME", request.userName());
        variables.put("URL", baseUrl + "auctions/auction/product_details/" + request.lotId());

        MailTemplate template = MailTemplate.BID_INFO_MAIL;

        SendEmailDTO dto = SendEmailDTO.builder()
                .email(request.email())
                .subject(template.getSubject())
                .htmlBody(getHtmlBody(template, variables))
                .build();
        mailSenderService.sendEmail(dto);
    }

    @Async
    public void sendVerificationMail(@Valid SendVerificationDTO request, UUID token) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("NAME", request.name());
        variables.put("URL", baseUrl + "verify/" + request.id() + '/' + token);

        MailTemplate template = MailTemplate.VERIFICATION_MAIL;

        SendEmailDTO dto = SendEmailDTO.builder()
                .email(request.email())
                .subject(template.getSubject())
                .htmlBody(getHtmlBody(template, variables))
                .build();
        mailSenderService.sendEmail(dto);
    }

    private String getHtmlBody(MailTemplate template, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        variables.put("LOGO_URL", logoUrl);
        variables.forEach(thymeleafContext::setVariable);

        return thymeleafTemplateEngine.process(template.getFileName(), thymeleafContext);
    }
}
