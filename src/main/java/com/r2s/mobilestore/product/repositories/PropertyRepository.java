package com.r2s.mobilestore.product.repositories;

import com.r2s.mobilestore.product.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
