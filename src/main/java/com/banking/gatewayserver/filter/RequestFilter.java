package com.banking.gatewayserver.filter;

import com.banking.gatewayserver.utility.FilterUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestFilter implements GlobalFilter {
    private final static Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    private final FilterUtility filterUtility;

    public RequestFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = filterUtility.getTraceId(exchange.getRequest().getHeaders())
                .orElseGet(filterUtility::generateTraceId);
        logger.debug("Trace-Id  of the current request is: {}", traceId);
        return chain.filter(exchange);
    }
}
