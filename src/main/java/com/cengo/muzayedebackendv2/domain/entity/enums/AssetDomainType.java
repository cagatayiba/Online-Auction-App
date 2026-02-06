package com.cengo.muzayedebackendv2.domain.entity.enums;


import java.util.Arrays;

public enum AssetDomainType {
    ARTIST_PROFILE(false, AssetMimeType.IMAGE),
    ARTIST_WORK(true, AssetMimeType.IMAGE, AssetMimeType.VIDEO),
    LOT_COVER(false, AssetMimeType.IMAGE),
    LOT_MEDIA(true, AssetMimeType.IMAGE, AssetMimeType.VIDEO),
    PRODUCT_COVER(false, AssetMimeType.IMAGE),
    PRODUCT_MEDIA(true, AssetMimeType.IMAGE, AssetMimeType.VIDEO),
    AUCTION_COVER_IMAGE(false, AssetMimeType.IMAGE),
    BLOG_COVER(false, AssetMimeType.IMAGE);


    private final Boolean deletable;
    private final AssetMimeType[] validMimes;

    AssetDomainType(Boolean deletable, AssetMimeType... validMimes) {
        this.deletable = deletable;
        this.validMimes = validMimes;
    }

    public Boolean isValid(AssetMimeType mimeType) {
        return Arrays.asList(validMimes).contains(mimeType);
    }

    public Boolean isDeletable() {
        return deletable;
    }

}
