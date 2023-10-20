package com.r2s.mobilestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.AddressService;
import com.r2s.mobilestore.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTests {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${address.address}${address.user}{userId}")
    private String getAllAddressByUserPoint;

    @Value("${address.address}${address.add}{userId}")
    private String addAddressPoint;

    @Value("${address.address}/{addressId}")
    private String editEndPoint;

    @MockBean
    private AddressService addressService;

    @Test
    @Transactional
    @Rollback
    void testGetAllAddressesByUser() throws Exception {
        // Define test data
        Long userId = 1L;
        User user = new User();
        user.setId(1L);

        // Tạo địa chỉ 1
        Address address1 = new Address();
        address1.setId(1L);
        address1.setName("Địa chỉ 1");
        address1.setPhoneNumber("1234567890");
        address1.setDeliveryAddress("123 Đường ABC");
        address1.setProvince("Tỉnh A");
        address1.setDistrict("Quận X");
        address1.setWard("Phường Y");
        address1.setLabel("Công ty");
        address1.setDefault(true); // Đây là địa chỉ mặc định

        // Tạo địa chỉ 2
        Address address2 = new Address();
        address2.setId(2L);
        address2.setName("Địa chỉ 2");
        address2.setPhoneNumber("0987654321");
        address2.setDeliveryAddress("456 Đường XYZ");
        address2.setProvince("Tỉnh B");
        address2.setDistrict("Quận Z");
        address2.setWard("Phường W");
        address2.setLabel("Nhà riêng");
        address2.setDefault(false); // Đây không phải là địa chỉ mặc định

        // Create a list of mock addresses
        List<Address> mockAddresses = new ArrayList<>();
        mockAddresses.add(address1);
        mockAddresses.add(address2);

        // Mock the behavior
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(addressService.findByUser(user)).thenReturn(mockAddresses);

        // Perform
        mockMvc.perform(get(getAllAddressByUserPoint, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(address1.getName()))
                .andExpect(jsonPath("$[0].phoneNumber").value(address1.getPhoneNumber()))
                .andExpect(jsonPath("$[0].deliveryAddress").value(address1.getDeliveryAddress()))
                .andExpect(jsonPath("$[0].province").value(address1.getProvince()))
                .andExpect(jsonPath("$[0].district").value(address1.getDistrict()))
                .andExpect(jsonPath("$[0].ward").value(address1.getWard()))
                .andExpect(jsonPath("$[0].label").value(address1.getLabel()))
                .andExpect(jsonPath("$[0].default").value(address1.isDefault()))
                .andExpect(jsonPath("$[1].name").value(address2.getName()))
                .andExpect(jsonPath("$[1].phoneNumber").value(address2.getPhoneNumber()))
                .andExpect(jsonPath("$[1].deliveryAddress").value(address2.getDeliveryAddress()))
                .andExpect(jsonPath("$[1].province").value(address2.getProvince()))
                .andExpect(jsonPath("$[1].district").value(address2.getDistrict()))
                .andExpect(jsonPath("$[1].ward").value(address2.getWard()))
                .andExpect(jsonPath("$[1].label").value(address2.getLabel()))
                .andExpect(jsonPath("$[1].default").value(address2.isDefault()));
    }

    @Test
    @Transactional
    @Rollback
    public void testAddAddress() throws Exception {
        // Define test data
        Long userId = 1L;
        Address address = new Address();
        address.setName("Test Address");
        address.setPhoneNumber("123456789");
        address.setDeliveryAddress("123 Main St");
        address.setProvince("Sample Province");
        address.setDistrict("Sample District");
        address.setWard("Sample Ward");
        address.setLabel("Home");
        address.setDefault(true); // Assuming isDefault is passed as a String "true"

        // Mock the behavior of userService.getUserById
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        doNothing().when(addressService).updateDefaultAddressesToFalse(any(Long.class));
        when(addressService.save(any(Address.class))).thenReturn(address);

        // Perform the POST request
        mockMvc.perform(post(addAddressPoint, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(address.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(address.getPhoneNumber()))
                .andExpect(jsonPath("$.deliveryAddress").value(address.getDeliveryAddress()))
                .andExpect(jsonPath("$.province").value(address.getProvince()))
                .andExpect(jsonPath("$.district").value(address.getDistrict()))
                .andExpect(jsonPath("$.ward").value(address.getWard()))
                .andExpect(jsonPath("$.label").value(address.getLabel()))
                .andExpect(jsonPath("$.default").value(address.isDefault()))
                .andDo(print());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Long addressId = 1L;
        // Define test data
        Address existingAddress = new Address();
        User user = new User();
        user.setId(1L);
        existingAddress.setId(addressId);
        existingAddress.setName("Old Name");
        existingAddress.setPhoneNumber("123456789");
        existingAddress.setDeliveryAddress("Old Delivery Address");
        existingAddress.setProvince("Old Province");
        existingAddress.setDistrict("Old District");
        existingAddress.setWard("Old Ward");
        existingAddress.setLabel("Old Label");
        existingAddress.setDefault(false);
        existingAddress.setUser(user);

        // Define test data
        Address updatedAddress = new Address();
        updatedAddress.setId(addressId);
        updatedAddress.setName("Updated Name");
        updatedAddress.setPhoneNumber("987654321");
        updatedAddress.setDeliveryAddress("Updated Delivery Address");
        updatedAddress.setProvince("Updated Province");
        updatedAddress.setDistrict("Updated District");
        updatedAddress.setWard("Updated Ward");
        updatedAddress.setLabel("Updated Label");
        updatedAddress.setDefault(true);

        // Mock the behavior
        when(addressService.getAddressById(addressId)).thenReturn(Optional.of(existingAddress));
        doNothing().when(addressService).updateDefaultAddressesToFalse(any(Long.class));
        when(addressService.save(any(Address.class))).thenReturn(updatedAddress);

        // Perform
        mockMvc.perform(put(editEndPoint, addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressId))
                .andExpect(jsonPath("$.name").value(updatedAddress.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedAddress.getPhoneNumber()))
                .andExpect(jsonPath("$.deliveryAddress").value(updatedAddress.getDeliveryAddress()))
                .andExpect(jsonPath("$.province").value(updatedAddress.getProvince()))
                .andExpect(jsonPath("$.district").value(updatedAddress.getDistrict()))
                .andExpect(jsonPath("$.ward").value(updatedAddress.getWard()))
                .andExpect(jsonPath("$.label").value(updatedAddress.getLabel()))
                .andExpect(jsonPath("$.default").value(updatedAddress.isDefault()))
                .andDo(print());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteAddress() throws Exception {
        // Mock the behavior of getAddressById to return an Address when called with a specific addressId
        Long addressId = 1L;
        Address addressToDelete = new Address();
        when(addressService.getAddressById(addressId)).thenReturn(Optional.of(addressToDelete));
        doNothing().when(addressService).deleteAddress(addressId);

        // Perform a DELETE request to delete the address with the specified addressId
        mockMvc.perform(delete(editEndPoint, addressId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}
