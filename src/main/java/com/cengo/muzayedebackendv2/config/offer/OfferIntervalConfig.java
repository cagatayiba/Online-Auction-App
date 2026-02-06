package com.cengo.muzayedebackendv2.config.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferIntervalConfig {

    @Bean
    public OfferInterval getOfferPriceIntervalChain() {
        return OfferInterval.constructChain(
                new OfferInterval(
                        1000,
                        10000,
                        100
                ),
                new OfferInterval(
                        10000,
                        100000,
                        500
                ),
                new OfferInterval(
                        100000,
                        250000,
                        2500
                ),
                new OfferInterval(
                        250000,
                        500000,
                        5000
                ),
                new OfferInterval(
                        500000,
                        10000000,
                        10000
                )
        );
    }
}