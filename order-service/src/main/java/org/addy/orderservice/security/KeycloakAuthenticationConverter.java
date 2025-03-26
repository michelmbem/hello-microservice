package org.addy.orderservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class KeycloakAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final DelegatingJwtGrantedAuthoritiesConverter delegatingConverter =
            new DelegatingJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter(), new KeycloakRolesConverter());

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(jwt, delegatingConverter.convert(jwt), jwt.getClaim("preferred_username"));
    }
}
