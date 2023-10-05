package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.user.dtos.EmailDTO;
import com.r2s.mobilestore.user.dtos.RegisterDTO;
import com.r2s.mobilestore.user.entities.OTP;
import com.r2s.mobilestore.user.services.OTPService;
import com.r2s.mobilestore.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * I will define UserControllerTests to unit test the Rest API endpoints
 * which has the following methods: sendOTP and createUser
 *
 * @author KhanhBD
 * @since 2023-10-05
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @MockBean
    private OTPService otpService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Value("${user.user}")
    private String endpoint;

    @Value("${user.user}${user.create}")
    private String getEndpoint;


    @Test
    @Transactional
    @Rollback
    public void shouldSendOTP() throws Exception {
        // Define test data for RegisterDTO here
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("buiduykhanh.tdc2020@gmail.com");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);
        Date expirationTime = calendar.getTime();

        when(otpService.createOrUpdateOTP(emailDTO.getEmail())).thenReturn(
                new OTP(1L,
                        "buiduykhanh.tdc2020@gmail.com",
                        "123456",
                        new Date(),
                        expirationTime
                ));

        // Perform the API request
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @Transactional
    @Rollback
    public void shouldReturnBadRequestWhenCreateUser() throws Exception {
        // Define test data for VerifyDTO here
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFullName("khanhbui");
        registerDTO.setPassword("123456@");
        registerDTO.setEmail("buiduykhanh.tdc2020@gmail.com");
        registerDTO.setOtpCode("123456");

        // Check invalid otp
        when(otpService.isOTPValid(registerDTO.getEmail(), registerDTO.getOtpCode())).thenReturn(false);

        // Perform the API request
        mockMvc.perform(post(getEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    @Rollback
    void shouldCreateUser() throws Exception {

        // Define test data for VerifyDTO here
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFullName("khanhbui");
        registerDTO.setPassword("123456@");
        registerDTO.setEmail("buiduykhanh.tdc2020@gmail.com");
        registerDTO.setOtpCode("123456");

        // check valid otp
        when(otpService.isOTPValid(registerDTO.getEmail(), registerDTO.getOtpCode())).thenReturn(true);

        mockMvc.perform(post(getEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated()).andDo(print());
    }

}
