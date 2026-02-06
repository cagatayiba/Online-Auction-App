package com.cengo.muzayedebackendv2.exception.message;

import lombok.Getter;

@Getter
public enum LoginErrorMessage implements BaseErrorMessage{
    INVALID_MAIL("Bu mail adresiyle kayıtlı bir kullanıcı bulunamadı."),
    INVALID_PASSWORD("Şifre doğru değil tekrar deneyiniz."),
    NOT_APPROVED("Mail gelen kutunuzu kontrol ediniz.");

    private final String message;
    private final String title = "Giriş Başarısız!";

    LoginErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return title + " | " + message;
    }
}
