package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.user.dtos.EmailDTO;
import com.r2s.mobilestore.user.dtos.RegisterDTO;
import com.r2s.mobilestore.user.dtos.UpdateUserDTO;
import com.r2s.mobilestore.user.entities.Otp;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.OTPService;
import com.r2s.mobilestore.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthenticationManager authManager;

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
                new Otp(1L,
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

    @Test
    void shouldGetUserByIdWhenFound() throws Exception {
        Long userId = 1L; // Thay thế bằng id người dùng thực tế
        User user = new User(); // Tạo đối tượng User dựa trên dữ liệu mong muốn
        user.setFullName("buiduykhanh");
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint +"/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andDo(print());
    }

    @Test
    void shouldGetUserByIdWhenNotFound() throws Exception {
        Long userId = 1L; 
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint +"/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldUpdateUserById() throws Exception {

        long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFullName("New FullName");
        updateUserDTO.setUsername("newUsername");
        updateUserDTO.setEmail("newemail@example.com");
        updateUserDTO.setGender("Male");
        updateUserDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        User updateUser = mapper.map(updateUserDTO, User.class);
        updateUser.setId(userId);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFullName("Old FullName");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldemail@example.com");
        existingUser.setGender("Female");
        existingUser.setDateOfBirth(LocalDate.of(1980, 1, 1));

        when(userService.get(userId)).thenReturn(Optional.of(existingUser));
        when(userService.save(existingUser)).thenReturn(updateUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(updateUserDTO.getFullName()))
                .andExpect(jsonPath("$.username").value(updateUserDTO.getUsername()))
                .andExpect(jsonPath("$.email").value(updateUserDTO.getEmail()))
                .andExpect(jsonPath("$.gender").value(updateUserDTO.getGender()))
                .andExpect(jsonPath("$.dateOfBirth").value(updateUserDTO.getDateOfBirth().toString()))
                .andDo(print());
    }

    @Test
    public void shouldUpdateUserByIdReturnNotFoundWhenUserIdNotFound() throws Exception {
        long nonExistentUserId = 999L;

        when(userService.get(nonExistentUserId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/user/{id}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateUserDTO())))
                .andExpect(status().isNotFound());
    }

}
