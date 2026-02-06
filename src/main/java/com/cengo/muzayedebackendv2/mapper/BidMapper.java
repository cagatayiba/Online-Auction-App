package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.BidDTO;
import com.cengo.muzayedebackendv2.domain.entity.Bid;
import com.cengo.muzayedebackendv2.domain.response.bid.BidLotResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BidMapper {

    Bid convertToBid(BidDTO dto, UUID lotId);

    BidLotResponse convertToBidLotResponse(Bid bid, String userFullName);
}
