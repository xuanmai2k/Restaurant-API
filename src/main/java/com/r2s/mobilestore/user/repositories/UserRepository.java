package com.r2s.mobilestore.user.repositories;

import com.r2s.mobilestore.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents a user repository all crud database methods
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullName(String fullName);

    Boolean existsByFullName(String fullName);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
