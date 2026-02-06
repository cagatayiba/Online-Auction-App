package com.cengo.muzayedebackendv2.config.properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class JsonPropertySource extends PropertySource<String> {
    private final Map<String, Object> properties = new HashMap<>();

    public JsonPropertySource(String name, String source) {
        super(name, source);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(source, new TypeReference<>() {});
            properties.putAll(map);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public Object getProperty(@Nonnull String name) {
        return properties.get(name);
    }
}
