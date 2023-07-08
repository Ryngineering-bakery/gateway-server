package com.cosangatha.bakery.gatewayserver.utils;


import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class FilterUtils {

    public static final String X_CORRELATION_ID = "x-correlation-id";

    public static String getCorrelationId(HttpHeaders requestHeaders) {
        if(requestHeaders.get(X_CORRELATION_ID) != null) {
            List<String> header = requestHeaders.get(X_CORRELATION_ID);
            return header.stream().findFirst().get();
        }

        return null;
    }


    public static ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return exchange.mutate()
                .request(
                        exchange
                                .getRequest()
                                .mutate()
                                .header(X_CORRELATION_ID,correlationId)
                                .build())
                .build();
    }
}
