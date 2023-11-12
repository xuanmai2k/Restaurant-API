package com.r2s.mobilestore.manufacturer.repositories;

import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Represents a manufacturer repository
 * all crud database methods
 *
 * @author xuanmai
 * @since 2023-11-07
 */
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    Page<Manufacturer> findByManufacturerNameContaining(String manufacturerName, PageRequest pageDTO);

    boolean existsByManufacturerName(String manufacturerName);
}
