package com.cengo.muzayedebackendv2.client.tckn;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcknVerifyRequest {

    public TcknVerifyRequest(Long idNumber, String name, String surname, Integer birthYear) {
        this.body = new SoapBody(new SoapRequest(idNumber, name, surname, birthYear));
    }

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    private SoapBody body;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SoapBody {
        @XmlElement(name = "TCKimlikNoDogrula", namespace = "http://tckimlik.nvi.gov.tr/WS")
        private SoapRequest request;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SoapRequest {
        @XmlElement(name = "TCKimlikNo", namespace = "http://tckimlik.nvi.gov.tr/WS")
        private Long idNumber;
        @XmlElement(name = "Ad", namespace = "http://tckimlik.nvi.gov.tr/WS")
        private String name;
        @XmlElement(name = "Soyad", namespace = "http://tckimlik.nvi.gov.tr/WS")
        private String surname;
        @XmlElement(name = "DogumYili", namespace = "http://tckimlik.nvi.gov.tr/WS")
        private Integer birthYear;

    }
}
