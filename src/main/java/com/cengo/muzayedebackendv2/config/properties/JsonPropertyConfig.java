package com.cengo.muzayedebackendv2.config.properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


public class JsonPropertyConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String jsonEnvVar = System.getenv("ENV_VARIABLES");
        if (jsonEnvVar != null) {
            environment.getPropertySources().addFirst(new JsonPropertySource("JSON Environment Variables", jsonEnvVar));
        }
    }
}
