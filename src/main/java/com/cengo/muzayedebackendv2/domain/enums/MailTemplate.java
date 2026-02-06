package com.cengo.muzayedebackendv2.domain.enums;

import lombok.Getter;

@Getter
public enum MailTemplate {
    VERIFICATION_MAIL("verification-mail.html", "Email Doğrulama"),
    BID_INFO_MAIL("bid-info-mail.html", "Teklif Geçildi"),
    BALANCE_MAIL("user-balance-mail.html", ""); //TODO

    private final String fileName;
    private final String subject;

    MailTemplate(String fileName, String subject) {
        this.fileName = fileName;
        this.subject = subject;
    }

}
