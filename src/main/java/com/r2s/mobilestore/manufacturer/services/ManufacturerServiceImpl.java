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
    @Override
    public Page<Manufacturer> listAll(PageDTO pageDTO) {
        return manufacturerRepository.findAll(PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public Optional<Manufacturer> getManufacturerById(long id) {
        return manufacturerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        manufacturerRepository.deleteById(id);
    }

    @Override
    public boolean checkForExistence(String manufacturerName) {
        return manufacturerRepository.existsByManufacturerName(manufacturerName);
    }

    @Override
    public Page<Manufacturer> search(String keyword, PageDTO pageDTO) {
        return manufacturerRepository.findByManufacturerNameContaining(keyword, PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }
}
