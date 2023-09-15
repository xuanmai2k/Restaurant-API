package com.r2s.sample.user.repositories;

import java.util.Optional;

import com.r2s.sample.user.entities.Role;
import com.r2s.sample.user.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository also extends JpaRepository and provides a finder method
 *
 * @author kyle
 * @since 2023-09-08
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
