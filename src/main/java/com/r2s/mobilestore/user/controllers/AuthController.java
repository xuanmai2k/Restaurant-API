package com.r2s.mobilestore.user.controllers;

import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.security.JwtTokenUtil;
import com.r2s.mobilestore.user.dtos.AuthDTO;
import com.r2s.mobilestore.user.dtos.JwtResponseDTO;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents a auth controller
 *
 * @author KhanhBD
 * @since 2023-10-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${user.user}")
public class AuthController {
    private final AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    private final ResponseDTO body = ResponseDTO.getInstance();

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
        try {
            var authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getEmail(), authDTO.getPassword()));

            var user = (User) authentication.getPrincipal();

            var token = jwtUtil.generateAccessToken(authentication);
            var jwtResponse = new JwtResponseDTO(token, Constants.AUTHORIZATION_TYPE, user.getEmail(), user.getRoles());

            // Successfully
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            logger.info(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}