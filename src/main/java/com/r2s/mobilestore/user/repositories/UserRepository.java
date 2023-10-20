package com.r2s.mobilestore.user.repositories;

import com.r2s.mobilestore.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Optional<User> findUserById(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR " +
            "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsersWithPagination(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}
