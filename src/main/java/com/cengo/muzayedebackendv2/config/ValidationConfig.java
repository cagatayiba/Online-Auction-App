package com.cengo.muzayedebackendv2.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

@Configuration
public class ValidationConfig {
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(LocalValidatorFactoryBean validator) {
        return (Map<String, Object> hibernateProperties) -> hibernateProperties.put("jakarta.persistence.validation.factory", validator);
    }
}
