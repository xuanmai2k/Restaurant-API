package com.r2s.mobilestore.security;

import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Represents JwtTokenUtil
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateAccessToken(Authentication authentication) {

        var scope = authentication.getAuthorities().stream().map(item
                -> new SimpleGrantedAuthority(item.getAuthority())).collect(Collectors.toList());

        var user = (User) authentication.getPrincipal();

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer(Constants.R2S)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(Constants.EXPIRE_DURATION))
                .subject(format("%s,%s", user.getId(), user.getEmail()))
                .claim("roles", scope)
                .claim("id", user.getId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
