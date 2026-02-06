package com.cengo.muzayedebackendv2.client.tckn;

import jakarta.xml.bind.annotation.*;
import lombok.*;


@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcknVerifyResponse {

	public boolean isSuccess(){
		return getBody().getResponse().getSuccess();
	}

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	private SoapBody body;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SoapBody {
		@XmlElement(name = "TCKimlikNoDogrulaResponse", namespace = "http://tckimlik.nvi.gov.tr/WS")
		private SoapResponse response;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SoapResponse {
		@XmlElement(name = "TCKimlikNoDogrulaResult", namespace = "http://tckimlik.nvi.gov.tr/WS")
		private Boolean success;

	}
}
