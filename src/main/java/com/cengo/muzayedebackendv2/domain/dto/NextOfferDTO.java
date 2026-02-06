package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.entity.Offer;

import java.util.Optional;

public record NextOfferDTO(
        Optional<Offer> offer,
        Integer price
) {
}
