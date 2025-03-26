package org.addy.orderservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class KeycloakRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
        if (!CollectionUtils.isEmpty(realmAccess)) {
            extractRoles(realmAccess, grantedAuthorities);
        }

        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");
        if (!CollectionUtils.isEmpty(resourceAccess)) {
            resourceAccess.forEach((resource, resourceClaims) ->
                    extractRoles(resourceClaims, grantedAuthorities));
        }

        return grantedAuthorities;
    }

    private static void extractRoles(Map<String, Collection<String>> accessClaim,
                                     Collection<GrantedAuthority> grantedAuthorities) {

        Collection<String> roles = accessClaim.get("roles");
        if (!CollectionUtils.isEmpty(roles)) {
            grantedAuthorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList());
        }
    }
}
