package com.cengo.muzayedebackendv2.util;

import com.cengo.muzayedebackendv2.config.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LotTimeManager {

    private final AppProperties appProperties;

    public int getAuctionExtraTime(LocalDateTime endTime, LocalDateTime extendedEndTime, LocalDateTime offerTime){
        if(extendedEndTime == null && offerTime.until(endTime, ChronoUnit.MINUTES) < 3){
            return appProperties.defaultExtraTime();
        }

        if (extendedEndTime != null && offerTime.until(extendedEndTime, ChronoUnit.MINUTES) < 1){
            return 1;
        }

        return 0;
    }
}