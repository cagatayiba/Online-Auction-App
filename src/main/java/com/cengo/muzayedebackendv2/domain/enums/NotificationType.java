package com.cengo.muzayedebackendv2.domain.enums;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;


public enum NotificationType {
    PRODUCT(" ürününe ait satın alımınız onaylandı."), //TODO link location
    BID(" teklifiniz geçildi.");

    @Setter
    private String baseUrl;
    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getLink(UUID id) {
        if (this == BID) return baseUrl + "hemen-al/" + id;
        return baseUrl;
    }

    public String getMessage(String name) {
        return name + message;
    }


    @Component
    @RequiredArgsConstructor
    public static class NotificationInitializer implements CommandLineRunner {

        @Value("${notification.baseUrl}")
        private String baseUrl;

        @Override
        public void run(String... args) {
            for (NotificationType type : NotificationType.values()) {
                type.setBaseUrl(baseUrl);
            }
        }
    }
}
