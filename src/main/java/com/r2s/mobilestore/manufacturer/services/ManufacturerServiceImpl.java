package com.r2s.mobilestore.manufacturer.services;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import com.r2s.mobilestore.manufacturer.repositories.ManufacturerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    /**
     * This method is used to list all manufacturers
     *
     * @param pageDTO This is a page
     * @return list all of manufacturers
     */
    @Override
    public Page<Manufacturer> listAll(PageDTO pageDTO) {
        return manufacturerRepository.findAll(PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }

    /**
     * This method is used to create a manufacturer
     *
     * @param manufacturer This is a manufacturer
     */
    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    /**
     * This method is used to get a manufacturer base on id
     *
     * @param id This is manufacturer id
     * @return manufacturer base on id
     */
    @Override
    public Optional<Manufacturer> getManufacturerById(long id) {
        return manufacturerRepository.findById(id);
    }

    /**
     * This method is used to delete a manufacturer by id
     *
     * @param id This is manufacturer id
     */
    @Override
    public void delete(Long id) {
        manufacturerRepository.deleteById(id);
    }

    /**
     * This method is used to check manufacturer name existence
     *
     * @param manufacturerName This is manufacturer name
     * @return boolean
     */
    @Override
    public boolean checkForExistence(String manufacturerName) {
        return manufacturerRepository.existsByManufacturerName(manufacturerName);
    }

    /**
     * This method is used to find manufacturers by keyword
     *
     * @param keyword This is keyword
     * @return List of manufacturers
     */
    @Override
    public Page<Manufacturer> search(String keyword, PageDTO pageDTO) {
        return manufacturerRepository.findByManufacturerNameContaining(keyword, PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }
}
