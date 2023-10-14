package com.r2s.mobilestore.product.repositories;

import com.r2s.mobilestore.product.entities.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    List<Manufacturer> findByManufacturerNameContaining(String manufacturerName);
}
