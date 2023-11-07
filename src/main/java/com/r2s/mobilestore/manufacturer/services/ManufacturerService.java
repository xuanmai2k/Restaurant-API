package com.r2s.mobilestore.manufacturer.services;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ManufacturerService {

    /**
     * This method is used to list all manufacturers
     *
     * @param pageDTO This is a page
     * @return list all of manufacturers
     */
    Page<Manufacturer> listAll(PageDTO pageDTO);

    /**
     * This method is used to create a manufacturer
     *
     * @param manufacturer This is a manufacturer
     */
    public Manufacturer save(Manufacturer manufacturer);

    /**
     * This method is used to get a manufacturer base on id
     *
     * @param id This is manufacturer id
     * @return manufacturer base on id
     */
    Optional<Manufacturer> getManufacturerById(long id);

    /**
     * This method is used to delete a manufacturer by id
     *
     * @param id This is manufacturer id
     */
    void delete(Long id);

    /**
     * This method is used to check manufacturer name existence
     *
     * @param manufacturerName This is manufacturer name
     * @return boolean
     */
    boolean checkForExistence(String manufacturerName);

    /**
     * This method is used to find manufacturers by keyword
     *
     * @param keyword This is keyword
     * @return List of manufacturers
     */
    Page<Manufacturer> search(String keyword, PageDTO pageDTO);
}
