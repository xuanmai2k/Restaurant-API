package com.r2s.mobilestore.config;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * These Roles Claim Converter
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
public class RolesClaimConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter wrappedConverter;

    public RolesClaimConverter(JwtGrantedAuthoritiesConverter conv) {
        wrappedConverter = conv;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // get authorities from wrapped converter
        var grantedAuthorities = new ArrayList<>(wrappedConverter.convert(jwt));
        // get role authorities
        var roles = (List<LinkedTreeMap>) jwt.getClaims().get("roles");

        if (roles != null) {
            for (LinkedTreeMap<String, String> role : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.get("role")));
            }
        }

        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }
}