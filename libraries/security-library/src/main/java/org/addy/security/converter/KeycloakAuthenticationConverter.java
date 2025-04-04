package org.addy.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class KeycloakAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    public static final String USERNAME_CLAIM = "preferred_username";

    private final DelegatingJwtGrantedAuthoritiesConverter delegate = new DelegatingJwtGrantedAuthoritiesConverter(
            new JwtGrantedAuthoritiesConverter(),
            new KeycloakAuthoritiesConverter()
    );

    @Override
    public @NonNull AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(jwt, delegate.convert(jwt), jwt.getClaim(USERNAME_CLAIM));
    }
}
