package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.security.JwtTokenUtil;
import com.r2s.mobilestore.user.dtos.EmailDTO;
import com.r2s.mobilestore.user.dtos.RegisterDTO;
import com.r2s.mobilestore.user.dtos.UpdateUserDTO;
import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.Otp;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.AddressService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.util.*;

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
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    @MockBean
    private OTPService otpService;

    @MockBean
    private UserService userService;

    @MockBean
    private AddressService addressService;

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

    @MockBean
    private JwtTokenUtil jwtUtil;

    @MockBean
    private Jwt jwt;

    @Value("${user.user}")
    private String endpoint;

    @Value("${user.user}${user.create}")
    private String creatEndpoint;

    @Value("${user.user}/{id}")
    private String getEndpoint;


    @Value("${user.user}${user.search}")
    private String searchEndpoint;

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

        // Mock the behavior
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
        mockMvc.perform(post(creatEndpoint)
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

        // Perform the API request
        mockMvc.perform(post(creatEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated()).andDo(print());
    }

    // ADD 2023/10/10 KhanhBD START
    @Test
    void shouldGetUserByIdWhenFound() throws Exception {
        // Define test data
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFullName("buiduykhanh");
        user.setEmail("khanh@gmail.com");

        // Mock the behavior
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andDo(print());
    }

    @Test
    void shouldGetUserByIdWhenNotFound() throws Exception {
        // Define test data
        Long userId = 1L;

        // Mock the behavior
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Perform the API request
        mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_ADMIN"})
    public void shouldUpdateUserById() throws Exception {
        // Define test data
        long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFullName("New FullName");
        updateUserDTO.setPhoneNumber("0123456789");
        updateUserDTO.setGender("Male");
        updateUserDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        User updateUser = mapper.map(updateUserDTO, User.class);
        updateUser.setId(userId);

        // Define test data
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFullName("Old FullName");
        existingUser.setPhoneNumber("9876543210");
        existingUser.setEmail("oldemail@example.com");
        existingUser.setGender("Female");
        existingUser.setDateOfBirth(LocalDate.of(1980, 1, 1));
        updateUser.setEmail(existingUser.getEmail());

        // Mock the behavior
        when(userService.get(userId)).thenReturn(Optional.of(existingUser));
        when(userService.save(existingUser)).thenReturn(updateUser);

        // Perform the API request
        mockMvc.perform(MockMvcRequestBuilders.put(getEndpoint, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateUser.getId()))
                .andExpect(jsonPath("$.fullName").value(updateUser.getFullName()))
                .andExpect(jsonPath("$.phoneNumber").value(updateUser.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()))
                .andExpect(jsonPath("$.gender").value(updateUser.getGender()))
                .andExpect(jsonPath("$.dateOfBirth").value(updateUser.getDateOfBirth().toString()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers() throws Exception {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1");
        user1.setFullName("User One");
        users.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2");
        user2.setFullName("User Two");
        users.add(user2);

        PageDTO pageDTO = new PageDTO();

        // Arrange
        Page<User> page = new PageImpl<>(users); // Create a Page instance with your list of users

        when(userService.getAllUsers(pageDTO)).thenReturn(page);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content", hasSize(users.size())))
                .andExpect(jsonPath("$.content[0].id").value(user1.getId()))
                .andExpect(jsonPath("$.content[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$.content[0].fullName").value(user1.getFullName()))
                .andExpect(jsonPath("$.content[1].id").value(user2.getId()))
                .andExpect(jsonPath("$.content[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.content[1].fullName").value(user2.getFullName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsersNoUsers() throws Exception {
        List<User> users = new ArrayList<>();

        PageDTO pageDTO = new PageDTO();

        // Arrange
        Page<User> page = new PageImpl<>(users); // Create a Page instance with your list of users

        when(userService.getAllUsers(pageDTO)).thenReturn(page);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    @Rollback
    public void testDeleteUserById() throws Exception {
        // Create address list
        List<Address> addresses = new ArrayList<>();
        Address address1 = new Address();
        address1.setId(1L);
        addresses.add(address1);

        Address address2 = new Address();
        address2.setId(2L);
        addresses.add(address2);

        // Arrange
        Long userId = 1L;

        // Mock the behavior of services
        doNothing().when(addressService).deleteAddress(anyLong());
        doNothing().when(userService).deleteUserById(anyLong());
        when(addressService.getAddressesByUserId(userId)).thenReturn(addresses);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(getEndpoint, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchUsers() throws Exception {
        // Create a PageDTO for pagination
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNumber(0);
        pageDTO.setPageSize(10);

        // Define a search term
        String searchTerm = "example";

        // Create a list of User objects to be returned
        List<User> users = new ArrayList<>();

        // Add some User objects to the list
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFullName("User One");
        // Set other user properties if needed
        users.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFullName("User Two");
        // Set other user properties if needed
        users.add(user2);

        // Arrange
        when(userService.searchUsersWithPagination(pageDTO, searchTerm)).thenReturn(new PageImpl<>(users));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get(searchEndpoint)
                        .param("searchTerm", searchTerm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(users.size())))
                .andExpect(jsonPath("$.content[0].id").value(user1.getId()))
                .andExpect(jsonPath("$.content[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$.content[0].fullName").value(user1.getFullName()))
                .andExpect(jsonPath("$.content[1].id").value(user2.getId()))
                .andExpect(jsonPath("$.content[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.content[1].fullName").value(user2.getFullName()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchUsers_NoResultsFound() throws Exception {
        // Create a PageDTO for pagination
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNumber(0);
        pageDTO.setPageSize(10);

        // Define a search term that is unlikely to have any matches
        String searchTerm = "nonexistent";

        // In this case, we assume that the search returns no results (an empty page).
        when(userService.searchUsersWithPagination(pageDTO, searchTerm)).thenReturn(Page.empty());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get(searchEndpoint)
                        .param("searchTerm", searchTerm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }


    // ADD 2023/10/10 KhanhBD END
}
