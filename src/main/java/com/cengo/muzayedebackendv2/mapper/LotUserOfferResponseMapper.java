package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.OfferWithBidsDTO;
import com.cengo.muzayedebackendv2.domain.response.lot.LotUserOfferResponse;
import com.cengo.muzayedebackendv2.mapper.config.DefaultMapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfiguration.class)
public interface LotUserOfferResponseMapper {

    @Mapping(target = "price", source = "offer.price")
    @Mapping(target = "time", source = "offer.time")
    LotUserOfferResponse convertToLotOfferResponse(OfferWithBidsDTO dto);
}
