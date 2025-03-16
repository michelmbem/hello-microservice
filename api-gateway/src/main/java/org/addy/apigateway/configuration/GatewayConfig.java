package org.addy.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r ->
                        r.path("/categories/**").or()
                                .path("/products/**").or()
                                .path("/product-images/**")
                                .uri("lb://product-service"))
                .build();
    }

}
