package com.banking.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class RouteConfig {
    private static final String ACCOUNTS_WITH_BAKNING_URI_PATH = "/banking/accounts/";
    private static final String CARDS_WITH_BAKNING_URI_PATH = "/banking/cards/";
    private static final String LOANS_WITH_BAKNING_URI_PATH = "/banking/loans/";
    private static final String ACCOUNTS_SERVICE_NAME = "ACCOUNTS";
    private static final String CARDS_SERVICE_NAME = "CARDS";
    private static final String LOANS_SERVICE_NAME = "LOANS";
    private static final String ACCOUNT_CIRCUIT_BREAKER = "accountCircuitBreaker";
    public static final String RESPONSE_TIME_HEADER = "X-Response-Time";

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(route -> route
                        .path(ACCOUNTS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(ACCOUNTS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName(ACCOUNT_CIRCUIT_BREAKER) // redirects /banking/accounts/api/create to /api/create
                                        .setFallbackUri("forward:/contactSupport")) // fallbacks to FallbackBackController instead of showing server side error to client
                                .addResponseHeader(RESPONSE_TIME_HEADER, Instant.now().toString()))
                        .uri("lb://"+ACCOUNTS_SERVICE_NAME)) // tells which service to find in the eureka discovery server
                .route(route -> route
                        .path(CARDS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(CARDS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}")
                                .addResponseHeader(RESPONSE_TIME_HEADER, Instant.now().toString()))
                        .uri("lb://"+CARDS_SERVICE_NAME))
                .route(route -> route
                        .path(LOANS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(LOANS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}")
                                .addResponseHeader(RESPONSE_TIME_HEADER, Instant.now().toString()))
                        .uri("lb://"+LOANS_SERVICE_NAME))
                .build();
    }
}
