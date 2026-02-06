package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.entity.Bid;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.response.bid.BidLotResponse;
import com.cengo.muzayedebackendv2.mapper.BidMapper;
import com.cengo.muzayedebackendv2.repository.BidRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BidService extends BaseEntityService<Bid, BidRepository> {

    private final BidMapper bidMapper;
    private final UserService userService;

    protected BidService(BidRepository repository, BidMapper bidMapper, UserService userService) {
        super(repository);
        this.bidMapper = bidMapper;
        this.userService = userService;
    }

    public void saveAllBids(List<Bid> newBids) {
        saveAll(newBids);
    }

    public int getLotBidCount(UUID lotId){
        return (int) getRepository().countByLotId(lotId);
    }

    public List<Bid> getAllBidsByLotId(UUID lotId) {
        return getRepository().findAllByLotIdOrderByCreateDate(lotId);
    }

    public List<Bid> getAllBidsByOfferId(UUID offerId){
        return getRepository().findAllByOfferId(offerId);
    }

    public List<BidLotResponse> getAllBidResponsesByLotId(List<Bid> bids, UUID requesterId) {
        return bids.stream()
                .map(bid -> {
                    if (requesterId.equals(bid.getUserId())) {
                        var user = userService.getUserById(bid.getUserId());
                        return bidMapper.convertToBidLotResponse(bid, user.getFullName());
                    }
                    return bidMapper.convertToBidLotResponse(bid, "******");
                }).toList();
    }

    public Bid getNextBid(UUID lotId, UUID userId) {
        getRepository().deleteAllByLotIdAndUserId(lotId, userId);
        return getRepository().findFirstByLotIdOrderByPriceDesc(lotId);
    }

    public Bid getNextBidInfo(UUID lotId, UUID userId) {
        return getRepository().findFirstByLotIdAndUserIdNotOrderByPriceDesc(lotId, userId);
    }

    public boolean isNextBidExists(UUID lotId, UUID userId, Integer currentLotPrice){
        return getRepository().existsByLotIdAndUserIdNotAndPriceLessThan(lotId, userId, currentLotPrice);
    }
}
