package com.r2s.sample.user.controllers;

import static java.lang.String.format;

import com.r2s.sample.enums.Response;
import com.r2s.sample.user.dtos.AuthDTO;
import com.r2s.sample.user.dtos.JwtResponseDTO;
import com.r2s.sample.user.entities.Role;
import com.r2s.sample.user.entities.User;
import com.r2s.sample.user.services.UserService;
import com.r2s.sample.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${user.user}")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Login handling
     *
     * @param authDTO This authentication dto
     * @return ResponseEntity
     */
    @PostMapping("${user.login}")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        Map<String, Object> jsonMap = new LinkedHashMap<>();

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getUsername(), authDTO.getPassword()));

            var user = (User) authentication.getPrincipal();
//            var roles = authentication.getAuthorities()
//                    .stream()
//                    .map(GrantedAuthority::getAuthority).toList();

            // Check role for show
            List<String> roles = user.getRoles().stream().map(item -> item.getName().toString()).toList();

            var now = Instant.now();

            var scope = user.getRoles().stream().map(item -> item.getName().toString()).collect(Collectors.joining(" "));

            var claims = JwtClaimsSet.builder()
                    .issuer("r2s.com.vn")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(Constants.EXPIRE_DURATION))
                    .subject(format("%s,%s", user.getId(), user.getUsername()))
                    .claim("roles", scope)
                    .build();

            var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            var jwtResponse = new JwtResponseDTO(token, Constants.AUTHORIZATION_TYPE, user.getEmail(), roles);

            // Successfully
            jsonMap.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonMap.put(Response.ResponseKey.DATA.getValue(), jwtResponse);

            return new ResponseEntity<>(jsonMap, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            logger.info(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}