package com.cengo.muzayedebackendv2.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt")
public record JwtProperties(
        String secretKey
) {
}
