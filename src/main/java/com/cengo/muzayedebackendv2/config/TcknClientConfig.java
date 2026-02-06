package com.cengo.muzayedebackendv2.config;

import com.cengo.muzayedebackendv2.client.tckn.TcknClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class TcknClientConfig {

    @Bean
    public TcknClient tcknClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://tckimlik.nvi.gov.tr/Service/KPSPublic.asmx")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add("SOAPAction", "http://tckimlik.nvi.gov.tr/WS/TCKimlikNoDogrula");
                    httpHeaders.add("Content-Type", "text/xml; charset=utf-8");
                })
                .build();

        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(TcknClient.class);
    }
}
