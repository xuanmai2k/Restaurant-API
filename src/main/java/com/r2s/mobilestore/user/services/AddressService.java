package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Represents address service
 * @author KhanhBD
 * @since 2023-10-09
 */
public interface AddressService {
    /**
     * This method is used to get addresses base on id
     *
     * @param id This is address id
     * @return address base on address id
     */
    Optional<Address> getAddressById(Long id);

    /**
     * This method is used to save address
     *
     * @param address This is address
     */
    Address save(Address address);

    /**
     * This method is used to delete address by id
     *
     * @param id This is address id
     */
    void deleteAddress(Long id);

    /**
     * This method is used to find list address base on user
     *
     * @param user This is user
     * @return list addresses base on user
     */
    List<Address> findByUser(User user);

    /**
     * This method is used to update Default Addresses To False
     *
     * @param userId This is userId
     */
    void updateDefaultAddressesToFalse(Long userId);

    /**
     * This method is used to get Addresses By UserId
     *
     * @param id This is userId
     */
    List<Address> getAddressesByUserId(Long id);
}