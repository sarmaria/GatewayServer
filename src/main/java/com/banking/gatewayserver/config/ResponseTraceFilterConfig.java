package com.banking.gatewayserver.config;

import com.banking.gatewayserver.utility.FilterUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilterConfig.class);

    @Bean
    public GlobalFilter responseTraceFilter(FilterUtility filterUtility) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                    String traceId = filterUtility.getTraceId(requestHeaders).get();
                    logger.debug("Updated the trace-id to the response: {} ", traceId);
                    exchange.getResponse().getHeaders().add(FilterUtility.TRACE_ID, traceId);
                }));
    }
}
