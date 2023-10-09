package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    List<Address> getAllAddresses();

    Optional<Address> getAddressById(Long id);

    Address save(Address address);

    void deleteAddress(Long id);

    List<Address> findByUser(User user);

    void updateDefaultAddressesToFalse(Long userId);
}