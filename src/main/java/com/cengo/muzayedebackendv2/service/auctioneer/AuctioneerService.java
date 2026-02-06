package com.cengo.muzayedebackendv2.service.auctioneer;

import com.cengo.muzayedebackendv2.config.offer.OfferInterval;
import com.cengo.muzayedebackendv2.domain.dto.BidDTO;
import com.cengo.muzayedebackendv2.domain.dto.OfferResultDTO;
import com.cengo.muzayedebackendv2.domain.entity.Offer;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuctioneerService {
    private final OfferInterval offerInterval;

    public OfferResultDTO getResult(Offer newOffer, Optional<Offer> currentOffer, Integer currentPrice) {
        List<BidDTO> givenBids = new ArrayList<>();

        if(currentOffer.isEmpty()) {
            addNewBid(givenBids, newOffer, currentPrice);
            return OfferResultDTO.builder()
                    .winnerOffer(newOffer)
                    .updatedPrice(currentPrice)
                    .hasNewOfferWon(true)
                    .isBuyerReplaced(false)
                    .givenBids(givenBids)
                    .build();
        }

        var buyerManager = BuyerManager.builder()
                .waitingBuyer(new Buyer(currentOffer.get(), true))
                .nextBuyer(new Buyer(newOffer, false))
                .build();

        while(true) {
            var nextPrice = increasePrice(currentPrice);
            var currentBuyer = buyerManager.getNextBuyer();

            if(!currentBuyer.raiseToNewPrice(nextPrice)) {
                if(currentBuyer.isInitialWinner() && buyerManager.isBuyerOffersSame()){
                    addNewBid(givenBids, currentBuyer.offer(), currentPrice);
                    buyerManager.switchBuyers();
                }
                break;
            }
            currentPrice = nextPrice;
            addNewBid(givenBids, currentBuyer.offer(), currentPrice);
        }

        var winner = buyerManager.getNextBuyer();

        return OfferResultDTO.builder()
                .winnerOffer(winner.offer())
                .updatedPrice(currentPrice)
                .hasNewOfferWon(winner.offer() == newOffer)
                .isBuyerReplaced(winner.getUserId() == newOffer.getUserId())
                .givenBids(givenBids)
                .build();
    }

    private void addNewBid(List<BidDTO> givenBids , Offer offer, Integer price) {
        var newBid = BidDTO.builder()
                .offerId(offer.getId())
                .userId(offer.getUserId())
                .price(price)
                .time(offer.getTime())
                .build();
        givenBids.add(newBid);
    }

    private int increasePrice(Integer price) {
        return offerInterval.getNextOfferPrice(price);
    }

    record Buyer(Offer offer, boolean isInitialWinner) {

        boolean raiseToNewPrice(int newPrice) {
            return offer.getPrice() >= newPrice;
        }

        UUID getUserId() {
            return offer.getUserId();
        }
    }

    @Builder
    static class BuyerManager{

        private Buyer waitingBuyer;
        private Buyer nextBuyer;

        public boolean isBuyerOffersSame(){
            return waitingBuyer.offer().getPrice().equals(nextBuyer.offer().getPrice());
        }

        public Buyer getNextBuyer() {
            var buyerToReturn = nextBuyer;
            switchBuyers();
            return buyerToReturn;
        }

        private void switchBuyers() {
            var temp = waitingBuyer;
            waitingBuyer = nextBuyer;
            nextBuyer = temp;
        }
    }
}
