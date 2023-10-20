package com.r2s.mobilestore.user.repositories;

import com.r2s.mobilestore.user.entities.Address;
import com.r2s.mobilestore.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Represents a Address Repository all crud database methods
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);

    Optional<List<Address>> findByUserIdAndIsDefaultTrue(Long userId);

    List<Address> findByUserId(Long userId);
}
