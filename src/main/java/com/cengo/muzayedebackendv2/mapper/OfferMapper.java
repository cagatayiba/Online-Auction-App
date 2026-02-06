package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.Offer;
import com.cengo.muzayedebackendv2.domain.request.OfferRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OfferMapper {
    Offer convertToOffer(OfferRequest offerRequest, UUID userId);
}
