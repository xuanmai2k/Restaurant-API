package com.r2s.sample.user.repositories;

import com.r2s.sample.category.entities.Category;
import com.r2s.sample.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents a user repository all crud database methods
 *
 * @author kyle
 * @since 2023-09-02
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //====== ADD 2023/09/05 kyle START ======//
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    //====== ADD 2023/09/05 kyle END ======//
}
