package com.cengo.muzayedebackendv2.exception.message;

import lombok.Getter;

@Getter
public enum ErrorMessage implements BaseErrorMessage {
    ITEM_NOT_FOUND("Verilen Id ile eşleşen kayıt bulunamadı.", "Id kontrol ediniz!"),
    MAIL_NOT_SEND("Mail servisi yanıt vermeyi durdurdu.", "Mail gönderilemedi!"),
    FORBIDDEN_REQUEST("Erişim reddedildi.", "Yetkisiz Erişim!"),
    TOKEN_EXPIRED("Tokenın süresi doldu.", "Token Hatası!"),
    TOKEN_INVALID("Token geçerli değil.", "Token Hatası!"),
    MULTIPART_FILE("Multipart isteği gerekli alanları gönderilmedi.", "Hatalı istek!"),

    FILE_NOT_UPLOADED("Dosya okunurken hata meydan geldi.", "Dosya yüklenemedi!"),
    ASSET_NOT_DELETED("Bu dosya tipi silinemez.", "Dosya silinemedi!"),
    ASSET_NOT_UPDATED("Taslak olamayan kaydın dosyası güncellenemez.", "Dosya güncellenemedi!"),
    ASSET_NOT_SAVED("Taslak olamayan kayda dosya eklenemez.", "Dosya eklenemedi!"),
    ASSET_ORDER_NOT_CHANGED("Farklı kayıtların dosyaları değiştirilemez.", "Dosyalar düzenlenemedi!"),
    ASSET_ORDER_SAME_ID("Değiştirilmek istenen dosyalar aynı.", "Dosyalar düzenlenemedi!"),

    AUCTION_NOT_UPDATED("Taslak olmayan müzayede değiştirilemez.", "Müzayede güncellenmedi!"),
    AUCTION_LOT_EMPTY("Eseri olmayan müzayede hazıra alınamaz.", "Müzayede güncellenmedi!"),
    AUCTION_STARTED("Başlayan müzayede taslağa alınamaz.", "Müzayede taslağa alınamadı!"),
    CURRENT_AUCTION_NOT_FOUND("Aktif müzayedemiz bulunmamaktadır. Yakında görüşmek dileğiyle..."),

    LOT_NOT_UPDATED("Taslak olmayan eser değiştirilemez.", "Eser güncellenmedi!"),

    PRODUCT_NOT_UPDATED("Taslak olmayan ürün değiştirilemez.", "Ürün güncellenmedi!"),
    PRODUCT_NOT_BOUGHT("Ürün satın alınmış.", "Ürün alınamadı!"),

    ARTIST_NOT_DELETED("Aktif ürünü olan sanatçı silinemez.", "Sanatçı silinemedi!"),

    PASSWORD_NOT_MATCH("Sifreler eşleşmiyor.", "Şifre güncellenemedi!"),

    LOT_STATE_NOT_SUITABLE_TO_WATCHLIST("Satışı devam etmeyen bir lot favorilere eklenemez.", "Favorilere Eklenemedi!"),

    INTERNAL_SERVER_ERROR("Beklenmedik bir hata alıyoruz. Lütfen daha sonra tekrar deneyiniz.", "İşlem Başarısız!");



    private final String message;
    private final String title;

    ErrorMessage(String message) {
        this.message = message;
        this.title = null;
    }

    ErrorMessage(String message, String title) {
        this.message = message;
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " | " + message;
    }
}
