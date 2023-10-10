package com.r2s.mobilestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.user.dtos.AddressDTO;
import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.AddressService;
import com.r2s.mobilestore.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * I will define AddressControllerTests to unit test the Rest API endpoints
 * which has the following methods: crud address
 *
 * @author KhanhBD
 * @since 2023-10-10
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTests {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${address.address}")
    private String endpoint;

    @Value("${address.user}")
    private String userPoint;

    @Value("${address.add}")
    private String addPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Mock
    private AddressService addressService;

    @Test
    public void testAddAddress() throws Exception {
        // Define test data
        Long userId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("Test Address");
        addressDTO.setPhoneNumber("123456789");
        addressDTO.setDeliveryAddress("123 Main St");
        addressDTO.setProvince("Sample Province");
        addressDTO.setDistrict("Sample District");
        addressDTO.setWard("Sample Ward");
        addressDTO.setLabel("Home");
        addressDTO.setIsDefault("true"); // Assuming isDefault is passed as a String "true"

        // Mock the behavior of userService.getUserById
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // Perform the POST request
        mockMvc.perform(post("/addresses/add/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(addressDTO.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(addressDTO.getPhoneNumber()))
                .andDo(print());
    }

    @Test
    @Transactional
    @Rollback
    void testGetAllAddressesByUser() throws Exception {
        Long userId = 1L;

        User user = new User();
        user.setId(1L);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get(endpoint + userPoint + "{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    void testGetAllAddresses() throws Exception {
        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Long addressId = 1L;
        AddressDTO updatedAddress = new AddressDTO();
        updatedAddress.setName("Updated Name");
        updatedAddress.setPhoneNumber("987654321");
        updatedAddress.setDeliveryAddress("Updated Delivery Address");
        updatedAddress.setProvince("Updated Province");
        updatedAddress.setDistrict("Updated District");
        updatedAddress.setWard("Updated Ward");
        updatedAddress.setLabel("Updated Label");
        updatedAddress.setIsDefault("true"); // Chuỗi boolean

        Address existingAddress = new Address();
        existingAddress.setId(addressId);
        existingAddress.setName("Old Name");
        existingAddress.setPhoneNumber("123456789");
        existingAddress.setDeliveryAddress("Old Delivery Address");
        existingAddress.setProvince("Old Province");
        existingAddress.setDistrict("Old District");
        existingAddress.setWard("Old Ward");
        existingAddress.setLabel("Old Label");
        existingAddress.setDefault(false);

        when(addressService.getAddressById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressService.save(existingAddress)).thenReturn(existingAddress);

        mockMvc.perform(put(endpoint+ "/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedAddress.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedAddress.getPhoneNumber()))
                .andExpect(jsonPath("$.deliveryAddress").value(updatedAddress.getDeliveryAddress()))
                .andExpect(jsonPath("$.province").value(updatedAddress.getProvince()))
                .andExpect(jsonPath("$.district").value(updatedAddress.getDistrict()))
                .andExpect(jsonPath("$.ward").value(updatedAddress.getWard()))
                .andExpect(jsonPath("$.label").value(updatedAddress.getLabel()))
                .andDo(print()); // Kiểm tra giá trị boolean là true
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteAddress() throws Exception {
        // Mock the behavior of getAddressById to return an Address when called with a specific addressId
        Long addressId = 1L;
        Address addressToDelete = new Address();
        when(addressService.getAddressById(addressId)).thenReturn(Optional.of(addressToDelete));

        // Perform a DELETE request to delete the address with the specified addressId
        mockMvc.perform(delete(endpoint + "/{addressId}", addressId))
                .andExpect(status().isNoContent())
                .andDo(print());

    }

}
