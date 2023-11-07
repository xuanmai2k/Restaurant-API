package com.r2s.mobilestore.manufacturer.services;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ManufacturerService {
    Page<Manufacturer> listAll(PageDTO pageDTO);

    public Manufacturer save(Manufacturer manufacturer);

    Optional<Manufacturer> getManufacturerById(long id);

    void delete(Long id);

    boolean checkForExistence(String manufacturerName);

    Page<Manufacturer> search(String keyword, PageDTO pageDTO);
}
