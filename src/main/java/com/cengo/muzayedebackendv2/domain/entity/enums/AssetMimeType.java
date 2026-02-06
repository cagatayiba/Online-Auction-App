package com.cengo.muzayedebackendv2.domain.entity.enums;

import org.springframework.web.multipart.MultipartFile;

public enum AssetMimeType {
    IMAGE,
    VIDEO,
    DEFAULT,
    INVALID;

    public static AssetMimeType fromFile(MultipartFile file) {
        if (file == null) {
            return DEFAULT;
        }
        var contentType = file.getContentType();
        if(contentType == null) return INVALID;

        var type = contentType.split("/")[0];
        return switch (type) {
            case "image" -> IMAGE;
            case "video" -> VIDEO;
            default -> INVALID;
        };
    }


}
