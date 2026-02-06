package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.entity.WatchList;
import com.cengo.muzayedebackendv2.domain.response.lot.LotWatchlistResponse;
import com.cengo.muzayedebackendv2.repository.WatchlistRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.validation.watchlist.WatchListSaveValidation;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

import static com.cengo.muzayedebackendv2.util.PageUtils.sortPage;

@Service
public class WatchlistService extends BaseEntityService<WatchList, WatchlistRepository> {

    private final WatchListSaveValidation watchListSaveValidation;
    private final LotService lotService;

    public WatchlistService(WatchlistRepository repository, WatchListSaveValidation watchListSaveValidation, LotService lotService) {
        super(repository);
        this.watchListSaveValidation = watchListSaveValidation;
        this.lotService = lotService;
    }


    public void saveWatchlist(UUID userId, UUID lotId) {
        watchListSaveValidation.validate(new WatchListSaveValidation.Context(lotId));

        WatchList watchlist = new WatchList(userId, lotId);
        save(watchlist);
    }

    @Transactional
    public void deleteWatchList(UUID userId, UUID lotId) {
        getRepository().deleteByUserIdAndLotId(userId, lotId);
    }

    public Page<LotWatchlistResponse> getUserWatchlist(UUID userId, Pageable pageable) {
        var watchLists = getRepository().findAllByUserId(userId, pageable);
        var unsortedResponse = watchLists.map(watchList -> lotService.getLotWatchListResponse(watchList.getLotId(), watchList.getUserId()));
        return sortPage(unsortedResponse, Comparator.comparingInt(LotWatchlistResponse::lotNumber).reversed());
    }

    public Boolean existsByUserIdAndLotId(UUID userId, UUID lotId) {
        return getRepository().findByUserIdAndLotId(userId, lotId).isPresent();
    }

    public void deleteBefore(LocalDateTime time) {
        getRepository().deleteByCreateDateBefore(time);
    }
}

