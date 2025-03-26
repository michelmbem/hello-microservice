package org.addy.orderservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class OAuthClientCredentialsFeignManager {
    private final Authentication principal = createPrincipal();
    private final OAuth2AuthorizedClientManager manager;
    private final ClientRegistration clientRegistration;

    public String getAccessToken() {
        try {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistration.getRegistrationId())
                    .principal(principal)
                    .build();

            OAuth2AuthorizedClient client = manager.authorize(authorizeRequest);
            if (isNull(client)) {
                throw new IllegalStateException("client credentials flow on " + clientRegistration.getRegistrationId() +
                        " failed, client is null");
            }

            return client.getAccessToken().getTokenValue();
        } catch (Exception e) {
            log.error("Client credentials error", e);
        }

        return null;
    }

    private Authentication createPrincipal() {
        return new Authentication() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // Does nothing
            }

            @Override
            public String getName() {
                return clientRegistration.getClientId();
            }
        };
    }
}
