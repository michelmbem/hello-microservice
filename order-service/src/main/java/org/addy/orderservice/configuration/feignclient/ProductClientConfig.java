package org.addy.orderservice.configuration.feignclient;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.addy.security.OAuthClientCredentialsFeignManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@RequiredArgsConstructor
@Configuration
public class ProductClientConfig {
    private static final String CLIENT_REGISTRATION_ID = "product-service";

    private final ClientRegistrationRepository registrationRepository;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    public RequestInterceptor productRequestInterceptor() {
        var credentialsManager = new OAuthClientCredentialsFeignManager(
                authorizedClientManager,
                registrationRepository.findByRegistrationId(CLIENT_REGISTRATION_ID)
        );

        return requestTemplate -> requestTemplate
                .header("Authorization", "Bearer " + credentialsManager.getAccessToken());
    }
}
