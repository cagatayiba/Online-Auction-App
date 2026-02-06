package com.cengo.muzayedebackendv2.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.UUID;


@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String awsBaseUrl,
        Integer offerLimit,
        Integer defaultExtraTime,
        Long lotEndTimeInterval,
        UUID observerUserId
) {
}
