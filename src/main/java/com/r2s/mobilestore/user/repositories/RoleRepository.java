package com.r2s.mobilestore.user.repositories;

import java.util.Optional;

import com.r2s.mobilestore.user.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository also extends JpaRepository and provides a finder method
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
