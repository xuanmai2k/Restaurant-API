package com.r2s.mobilestore.user.controllers;

import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.user.dtos.AddressDTO;
import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.AddressService;
import com.r2s.mobilestore.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Represents a address controller
 *
 * @author KhanhBD
 * @since 2023-10-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${address.address}")
public class AddressController {

    /**
     * Create a user Service
     */
    @Autowired
    private UserService userService;

    /**
     * Create a address Service
     */
    @Autowired
    private AddressService addressService;

    /**
     * This will help to convert Entity to DTO and vice versa automatically.
     */
    @Autowired
    private ModelMapper mapper;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Build add Address by user id
     *
     * @param userId This is user id
     * @param addressDTO This is addressDTO
     * @return status add Address
     */

    @PostMapping("${address.add}{userId}")
    public ResponseEntity<Address> addAddress(@PathVariable Long userId, @RequestBody AddressDTO addressDTO) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Convert DTO to an entity
        Address address = mapper.map(addressDTO, Address.class);
        addressService.updateDefaultAddressesToFalse(userId);
        address.setDefault(Boolean.parseBoolean(addressDTO.getIsDefault()));
        address.setUser(user);

        Address savedAddress = addressService.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    /**
     * Build get All Address by user id
     *
     * @param userId This is user id
     * @return status get All Address and list Address base on user id
     */
    @GetMapping("${address.user}{userId}")
    public ResponseEntity<?> getAllAddressesByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Address> addresses = addressService.findByUser(user);

        // Category is found
        if (!addresses.isEmpty()) {
            addresses.sort(Comparator.comparing(Address::isDefault).reversed());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        }

        body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
    }

    /**
     * Build get All Address
     *
     * @return status and list Address
     */
    @GetMapping
    public ResponseEntity<?> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();

        // address is found
        if (!addresses.isEmpty()) {
            addresses.sort(Comparator.comparing(Address::isDefault).reversed());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        }

        body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
    }

    /**
     * Build add Address by user id
     *
     * @param addressId This is address id
     * @param updatedAddress This is addressDTO
     * @return status add update Address
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO updatedAddress) {
        Address existingAddress = addressService.getAddressById(addressId).orElse(null);
        if (existingAddress == null) {
            return ResponseEntity.notFound().build();
        }

        // Update address fields as needed
        existingAddress.setName(updatedAddress.getName());
        existingAddress.setPhoneNumber(updatedAddress.getPhoneNumber());
        existingAddress.setDeliveryAddress(updatedAddress.getDeliveryAddress());
        existingAddress.setProvince(updatedAddress.getProvince());
        existingAddress.setDistrict(updatedAddress.getDistrict());
        existingAddress.setWard(updatedAddress.getWard());
        existingAddress.setLabel(updatedAddress.getLabel());
        boolean isDefault = Boolean.parseBoolean(updatedAddress.getIsDefault());
        existingAddress.setDefault(isDefault);

        Address savedAddress = addressService.save(existingAddress);

        return ResponseEntity.ok(savedAddress);
    }

    /**
     * Build delete Address by address id
     *
     * @param addressId This is address id
     * @return status delete Address
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId).orElse(null);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }

        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
