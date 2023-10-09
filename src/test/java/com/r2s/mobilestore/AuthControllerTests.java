package com.r2s.mobilestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.user.dtos.AuthDTO;
import com.r2s.mobilestore.user.entities.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * I will define AuthControllerTests to unit test the Rest API endpoints
 * which has the following methods: login
 *
 * @author KhanhBD
 * @since 2023-10-08
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Value("${user.user}")
    private String endpoint;

    @Value("${user.user}${user.login}")
    private String getEndpoint;


    @Test
    @Transactional
    @Rollback
    public void testSuccessfulLogin() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("admin@gmail.com");
        authDTO.setPassword("admin");

        User user = new User();
        user.setEmail(authDTO.getEmail());
        user.setPassword(authDTO.getPassword());

        // Mock authenticationManager.authenticate() to return a user
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));

        // Perform the API request
        mockMvc.perform(post(getEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testFailedLogin() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("user@example.com");
        authDTO.setPassword("invalidPassword");

        // Mock authenticationManager.authenticate() to throw BadCredentialsException
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Perform the API request
        mockMvc.perform(post(getEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized()).andDo(print());
    }
}
