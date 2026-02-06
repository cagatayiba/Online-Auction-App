package com.cengo.muzayedebackendv2.domain.enums;

import lombok.Getter;

@Getter
public enum PointBenefitAction {
    SUCCESSFUL_OFFER_GIVEN("Başarılı Teklif Verme", "Kullanıcı Başarılı Teklif Verir", "Başarılı bir teklif Verdiğinizde {0} puan"),
    PRODUCT_BOUGHT("Ürün Satın Alma", "Kullanıcı Ürün Satın Alır", "Ürün Satın Aldığınızda {0} puan");

   private final String text;
   private final String description;
   private final String userMessage;

    PointBenefitAction(String text, String description, String userMessage){
        this.text = text;
        this.description = description;
        this.userMessage = userMessage;
    }
}
