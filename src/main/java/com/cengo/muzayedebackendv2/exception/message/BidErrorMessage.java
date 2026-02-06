package com.cengo.muzayedebackendv2.exception.message;

import lombok.Getter;

@Getter
public enum BidErrorMessage implements BaseErrorMessage{
    AUCTION_NOT_STARTED("Başlamamış bir müzayedeye teklif verilemez."),
    LOT_STATE_NOT_SUITABLE_FOR_OFFER("Lot teklif vermeye uygun değil."),
    LOT_SALE_TIME_ENDED("Lot için teklif verme süresi bitti."),
    OFFER_NOT_GRATER_THAN_LOT_PRICE("Teklifiniz eserin güncel fiyatından fazla olmalı."),
    INVALID_OFFER_PRICE("Teklifiniz eserin güncel fiyatına uygun değil."),
    BID_CONFLICT("Çok fazla teklif alıyoruz. Lütfen tekrar deneyiniz.");

    private final String message;
    private final String title = "Teklifiniz alınamadı!";

    BidErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return title + " | " + message;
    }
}
