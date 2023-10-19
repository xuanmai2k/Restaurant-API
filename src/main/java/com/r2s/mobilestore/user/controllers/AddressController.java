package com.r2s.mobilestore.user.controllers;

import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.services.AddressService;
import com.r2s.mobilestore.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Build add Address by user id
     *
     * @param userId This is user id
     * @param address This is address
     * @return status add Address
     */
    @PostMapping("${address.add}{userId}")
    public ResponseEntity<?> addAddress(@PathVariable Long userId, @RequestBody Address address) {
        try {
            User user = userService.getUserById(userId).orElse(null);
            if (user == null) {
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
            }

            addressService.updateDefaultAddressesToFalse(userId);
            address.setUser(user);

            Address savedAddress = addressService.save(address);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build get All Address by user id
     *
     * @param userId This is user id
     * @return status get All Address and list Address base on user id
     */
    @GetMapping("${address.user}{userId}")
    public ResponseEntity<?> getAllAddressesByUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId).orElse(null);
            if (user == null) {
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
            }

            List<Address> addresses = addressService.findByUser(user);

            // Category is found
            if (!addresses.isEmpty()) {
                addresses.sort(Comparator.comparing(Address::isDefault).reversed());
                return new ResponseEntity<>(addresses, HttpStatus.OK);
            }

            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build get Address by id
     *
     * @param id This is id
     * @return status and Address base on user id
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getAddressesById(@PathVariable Long id) {
        try {
            Address existingAddress = addressService.getAddressById(id).orElse(null);

            if (existingAddress == null) {
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(existingAddress, HttpStatus.OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build add Address by user id
     *
     * @param addressId This is address id
     * @param updatedAddress This is addressDTO
     * @return status add update Address
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        try {
            Address existingAddress = addressService.getAddressById(addressId).orElse(null);
            if (existingAddress == null) {
                return ResponseEntity.notFound().build();
            }

            addressService.updateDefaultAddressesToFalse(existingAddress.getUser().getId());

            // Update address fields as needed
            existingAddress.setName(updatedAddress.getName());
            existingAddress.setPhoneNumber(updatedAddress.getPhoneNumber());
            existingAddress.setDeliveryAddress(updatedAddress.getDeliveryAddress());
            existingAddress.setProvince(updatedAddress.getProvince());
            existingAddress.setDistrict(updatedAddress.getDistrict());
            existingAddress.setWard(updatedAddress.getWard());
            existingAddress.setLabel(updatedAddress.getLabel());
            existingAddress.setDefault(updatedAddress.isDefault());

            Address savedAddress = addressService.save(existingAddress);

            return new ResponseEntity<>(savedAddress, HttpStatus.OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build delete Address by address id
     *
     * @param addressId This is address id
     * @return status delete Address
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        try {
            Address address = addressService.getAddressById(addressId).orElse(null);
            if (address == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            addressService.deleteAddress(addressId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
