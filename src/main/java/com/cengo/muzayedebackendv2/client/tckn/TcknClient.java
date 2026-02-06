package com.cengo.muzayedebackendv2.client.tckn;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TcknClient {

    @PostExchange
    TcknVerifyResponse verify(@RequestBody TcknVerifyRequest envelope);
}
