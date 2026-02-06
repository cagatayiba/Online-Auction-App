package com.cengo.muzayedebackendv2.config.offer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OfferInterval {

    private final Logger logger = LoggerFactory.getLogger(OfferInterval.class);

    private final int minPrice;
    private final int maxPrice;
    private final int raiseAmount;
    private OfferInterval nextInterval;

    public OfferInterval(int minPrice, int maxPrice, int raiseAmount) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.raiseAmount = raiseAmount;
    }

    public List<Integer> getOfferPrices(Integer currentPrice, Integer limit, boolean includeCurrentPrice) {
        List<Integer> offerPrices = new ArrayList<>();

        var desiredNumberOfPrices = limit;
        if(includeCurrentPrice){
            offerPrices.add(currentPrice);
            desiredNumberOfPrices--;
        }

        var nextPrice = currentPrice;
        for (var i = 0; i < desiredNumberOfPrices; i++) {
            nextPrice = getNextOfferPrice(nextPrice);
            offerPrices.add(nextPrice);
        }
        return offerPrices;
    }

    public Boolean isOfferInLimit(Integer offerPrice, Integer currentPrice, Integer limit) {
        if (Objects.equals(offerPrice, currentPrice)) {
            return true;
        }
        var nextPrice = currentPrice;
        for (var i = 0; i < limit; i++) {
            nextPrice = getNextOfferPrice(nextPrice);
            if (nextPrice.equals(offerPrice)) {
                return true;
            }
        }
        return false;
    }

    public int getNextOfferPrice(Integer price) {
        var nextPrice = price + raiseAmount;
        if (contains(nextPrice)) {
            return nextPrice;
        }
        if (nextInterval != null) {
            return nextInterval.getNextOfferPrice(price);
        }
        logger.warn("Price ({}) is out of range for intervals {}", nextPrice, this);
        throw new NoSuchElementException();
    }

    private boolean contains(int price) {
        return minPrice <= price && price < maxPrice;
    }

    private void linkWith(OfferInterval next){
        nextInterval = next;
    }

    protected static OfferInterval constructChain(OfferInterval first, OfferInterval... subsequentIntervals) {
        var head = first;
        for(OfferInterval nextInterval : subsequentIntervals) {
            head.linkWith(nextInterval);
            head = nextInterval;
        }
        return first;
    }

    @Override
    public String toString() {
        return "Interval: " + minPrice + " - " + maxPrice;
    }
}