package com.cengo.muzayedebackendv2.service.scheduler;

import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.verification.VerificationToken;
import com.cengo.muzayedebackendv2.service.AuctionService;
import com.cengo.muzayedebackendv2.service.LotService;
import com.cengo.muzayedebackendv2.service.WatchlistService;
import com.cengo.muzayedebackendv2.service.verification.VerificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    private final VerificationService verificationService;
    private final AuctionService auctionService;
    private final LotService lotService;
    private final WatchlistService watchlistService;


    @Scheduled(fixedDelay = 10*60*1000)
    public void deleteExpiredTokens() {
        List<VerificationToken> expiredTokens = verificationService.getAllExpiredTokens(LocalDateTime.now());
        logger.info("Deleting expired tokens: {}", expiredTokens);
        verificationService.deleteAllTokens(expiredTokens);
    }

    @Scheduled(cron = "0 * * * * *")
    public void auctionHandler() {
        startAuction();
        handleCurrentAuction();
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void watchlistHandler() {
        clearWatchlist();
    }

    private void clearWatchlist(){
        auctionService.findCurrentAuction()
                .ifPresent(currentAuction -> watchlistService.deleteBefore(currentAuction.getStartTime()));
    }

    private void startAuction() {
        var candidateAuction = auctionService.findReadyAuction();
        candidateAuction.ifPresent(auction -> auctionService.startAuction(auction.getId()));
    }

    private void handleCurrentAuction() {
        var currentAuction = auctionService.findCurrentAuction();

        if(currentAuction.isPresent()) {
            var currentAuctionId = currentAuction.get().getId();
            var lots = handleCurrentLots(currentAuctionId);
            endAuctionIfNecessary(currentAuctionId, lots);
        }
    }

    private List<Lot> handleCurrentLots(UUID auctionId) {
        return lotService.endLots(auctionId);
    }

    private void endAuctionIfNecessary(UUID auctionId, List<Lot> lots) {
        var isLotsEnd = lots.stream().allMatch(lot -> lot.getState().isEnded());

        if (isLotsEnd) {
            auctionService.endAuction(auctionId);
        }
    }
}
