package com.cengo.muzayedebackendv2.config.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class RSQLConfig {

    public static final ComparisonOperator
            LIKE = new ComparisonOperator("=like=", false),
            ILIKE = new ComparisonOperator("=ilike=", false),
            SILIKE = new ComparisonOperator("=silike=", false);


    @Bean
    public Set<ComparisonOperator> rsqlCustomOperators() {
        return Set.of(LIKE, ILIKE, SILIKE);
    }

}
