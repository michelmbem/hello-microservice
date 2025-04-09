package org.addy.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class OAuthClientCredentialsFeignManager {
    private final ClientRegistration clientRegistration;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final Authentication principal = createPrincipal();

    public String getAccessToken() {
        try {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistration.getRegistrationId())
                    .principal(principal)
                    .build();

            OAuth2AuthorizedClient client = authorizedClientManager.authorize(authorizeRequest);
            if (client == null) {
                throw new IllegalStateException(String.format(
                        "Client credentials flow on %s failed, client is null",
                        clientRegistration.getRegistrationId()));
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
                return Set.of();
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
