package com.cosangatha.bakery.gatewayserver.filters;

import com.cosangatha.bakery.gatewayserver.utils.FilterUtils;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(2)
@Slf4j
public class PostProcessingFilter implements GlobalFilter {

    @Autowired
    private Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        This is a Post filter .. Check the chain invokes the filter ( resource service ) and then performs business logic ( add correlation id )
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = FilterUtils.getCorrelationId(requestHeaders);
//            String tracerId = this.tracer.currentSpan().context().traceId();
            exchange.getResponse().getHeaders().add(FilterUtils.X_CORRELATION_ID, correlationId);
            log.info("x-correlation-id set to tracerId and added to response header : {} ", correlationId);
        }));
    }
}
