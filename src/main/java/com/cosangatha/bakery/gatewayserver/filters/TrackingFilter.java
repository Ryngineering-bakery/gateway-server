package com.cosangatha.bakery.gatewayserver.filters;

import com.cosangatha.bakery.gatewayserver.utils.FilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class TrackingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        log.info("RequestHeaders : {} " , requestHeaders);
        if(FilterUtils.getCorrelationId(requestHeaders) != null) {
            log.info("x-correlation-id from gateway , {} ", FilterUtils.getCorrelationId(requestHeaders));
        } else {
            exchange = FilterUtils.setCorrelationId(exchange, UUID.randomUUID().toString());
            log.info("new x-correlation-id generated from gateway , {} ", FilterUtils.getCorrelationId(requestHeaders));
        }

        return chain.filter(exchange);
    }
}
