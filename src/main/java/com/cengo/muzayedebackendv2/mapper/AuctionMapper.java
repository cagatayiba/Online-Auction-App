package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.request.AuctionSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.AuctionUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.AuctionAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.AuctionAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionListResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionWinningResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotAuctionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AuctionMapper {

    Auction convertToAuction(AuctionSaveRequest request);

    @Mapping(target = "id", source = "auction.id")
    AuctionListResponse convertToAuctionListResponse(Auction auction, AssetResponse coverAsset, Integer remainingTime, String infoText);

    @Mapping(target = "id", source = "auction.id")
    AuctionWinningResponse convertToAuctionWinningResponse(Auction auction, AssetResponse coverAsset);

    @Mapping(target = "id", source = "auction.id")
    AuctionAdminResponse convertToAuctionAdminResponse(Auction auction, AssetResponse coverAsset);

    AuctionAdminListResponse convertToAuctionListResponse(Auction auction);

    AuctionResponse convertToAuctionListResponse(Page<LotAuctionResponse> lots);

    void updateAuction(@MappingTarget Auction auction, AuctionUpdateRequest auctionUpdateRequest);

}
