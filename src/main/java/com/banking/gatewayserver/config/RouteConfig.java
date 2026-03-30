package com.banking.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    private static final String ACCOUNTS_WITH_BAKNING_URI_PATH = "/banking/accounts/";
    private static final String CARDS_WITH_BAKNING_URI_PATH = "/banking/cards/";
    private static final String LOANS_WITH_BAKNING_URI_PATH = "/banking/loans/";
    private static final String ACCOUNTS_SERVICE_NAME = "ACCOUNTS";
    private static final String CARDS_SERVICE_NAME = "CARDS";
    private static final String LOANS_SERVICE_NAME = "LOANS";

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(route -> route
                        .path(ACCOUNTS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(ACCOUNTS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}"))
                        .uri("lb://"+ACCOUNTS_SERVICE_NAME))
                .route(route -> route
                        .path(CARDS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(CARDS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}"))
                        .uri("lb://"+CARDS_SERVICE_NAME))
                .route(route -> route
                        .path(LOANS_WITH_BAKNING_URI_PATH +"**")
                        .filters(uri -> uri.rewritePath(LOANS_WITH_BAKNING_URI_PATH+"(?<segment>.*)", "/${segment}"))
                        .uri("lb://"+LOANS_SERVICE_NAME))
                .build();
    }
}
