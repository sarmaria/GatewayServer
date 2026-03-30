package com.banking.gatewayserver.utility;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FilterUtility {

    public static final String TRACE_ID = "trace-id";

    public Optional<String> getTraceId(HttpHeaders requestHeaders) {
        return Optional.ofNullable(requestHeaders.get(TRACE_ID))
                .orElse(List.of())
                .stream()
                .findFirst();
    }

    public ServerWebExchange setRequestHeaders(ServerWebExchange exchange, String headerName, String headerValue) {
        return exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header(headerName, headerValue)
                        .build())
                .build();
    }

    public String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
