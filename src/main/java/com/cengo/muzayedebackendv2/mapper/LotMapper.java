package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.LotAuctionResponseDTO;
import com.cengo.muzayedebackendv2.domain.dto.LotResponseDTO;
import com.cengo.muzayedebackendv2.domain.dto.LotWatchlistResponseDTO;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.request.LotSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.LotUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminSaleResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistLotResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.*;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;
import com.cengo.muzayedebackendv2.mapper.config.DefaultMapperConfiguration;
import org.mapstruct.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(config = DefaultMapperConfiguration.class, uses = {LotUserOfferResponseMapper.class})
public interface LotMapper {

    Lot convertToLot(LotSaveRequest request);

    @Mapping(target = "id", source = "lot.id")
    @Mapping(target = "winnerOfferId", source = "lot.winnerOfferId", qualifiedByName = "mapWinner")
    @Mapping(target = "name", source = "lot.name")
    LotAdminResponse convertToLotAdminResponse(Lot lot, AssetResponse coverImage,
                                               List<AssetResponse> mediaAssets, String artistName);

    LotAdminSaleResponse convertToLotAdminSaleResponse(LotAdminResponse lot, UserBuyerResponse buyer,
                                                       Boolean isTransferEnabled);

    LotResponse convertToLotResponse(Lot lot, LotResponseDTO dto);

    @Mapping(target = "id", source = "lot", qualifiedByName = "mapId")
    @Mapping(target = "currentPrice", source = "lot", qualifiedByName = "mapCurrentPrice")
    @Mapping(target = "bidCount", source = "dto", qualifiedByName = "mapBidCount")
    @Mapping(target = "saleEndTime", source = "lot", qualifiedByName = "mapEndTime")
    LotAuctionResponse convertToLotAuctionResponse(Lot lot, LotAuctionResponseDTO dto, @Context boolean isCurrent);

    @Mapping(target = "id", source = "lot", qualifiedByName = "mapId")
    @Mapping(target = "currentPrice", source = "lot", qualifiedByName = "mapCurrentPrice")
    @Mapping(target = "bidCount", source = "dto", qualifiedByName = "mapBidCount")
    @Mapping(target = "saleEndTime", source = "lot", qualifiedByName = "mapEndTime")
    LotWatchlistResponse convertToLotWatchlistResponse(Lot lot, LotWatchlistResponseDTO dto, @Context boolean isCurrent);

    LotWinningResponse convertToLotWinningResponse(Lot lot, AssetResponse coverImage,
                                                   List<AssetResponse> mediaAssets, ArtistLotResponse artistInfo);

    void updateLot(@MappingTarget Lot lot, LotUpdateRequest lotUpdateRequest);


    @Named("mapWinner")
    default UUID mapWinner(Optional<UUID> winnerOfferId) {
        return winnerOfferId.orElse(null);
    }

    @Named("mapId")
    default UUID mapId(Lot lot, @Context boolean isCurrent) {
        if (isCurrent) {
            return lot.getId();
        }
        return null;
    }

    @Named("mapBidCount")
    default Integer mapBidCount(LotAuctionResponseDTO dto, @Context boolean isCurrent) {
        if (isCurrent) {
            return dto.bidCount();
        }
        return null;
    }

    @Named("mapBidCount")
    default Integer mapBidCount(LotWatchlistResponseDTO dto, @Context boolean isCurrent) {
        if (isCurrent) {
            return dto.bidCount();
        }
        return null;
    }

    @Named("mapEndTime")
    default LocalDateTime mapEndTime(Lot lot, @Context boolean isCurrent) {
        if (isCurrent) {
            return lot.getSaleEndTime();
        }
        return null;
    }

    @Named("mapCurrentPrice")
    default Integer mapCurrentPrice(Lot lot, @Context boolean isCurrent) {
        if (isCurrent) {
            return lot.getCurrentPrice();
        }
        return null;
    }
}
