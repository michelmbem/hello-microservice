package org.addy.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public @NonNull Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
        if (!CollectionUtils.isEmpty(realmAccess)) {
            extractRoles(realmAccess, grantedAuthorities);
        }

        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");
        if (!CollectionUtils.isEmpty(resourceAccess)) {
            resourceAccess.forEach((resourceId, claims) ->
                    extractRoles(claims, grantedAuthorities));
        }

        return grantedAuthorities;
    }

    private static void extractRoles(Map<String, Collection<String>> claims,
                                     Collection<GrantedAuthority> authorities) {

        Collection<String> roles = claims.get("roles");
        if (!CollectionUtils.isEmpty(roles)) {
            authorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList());
        }
    }
}
