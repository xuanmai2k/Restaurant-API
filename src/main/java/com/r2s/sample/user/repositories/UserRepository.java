package com.r2s.sample.user.repositories;

import com.r2s.sample.category.entities.Category;
import com.r2s.sample.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents a user repository
 * all crud database methods
 *
 * @author kyle
 * @since 2023-09-02
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
