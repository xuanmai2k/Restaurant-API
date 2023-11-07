package com.r2s.mobilestore.manufacturer.controllers;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import com.r2s.mobilestore.manufacturer.services.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Represents a manufacturer controller
 *
 * @author xuanmai
 * @since 2023-11-07
 */
@RestController
@RequestMapping("${manufacturer}")
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(ManufacturerController.class);

    /**
     * REST API methods for Retrieval operations
     *
     * @param pageDTO This is a page
     * @return list all of promotions
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllManufacturer(@RequestBody PageDTO pageDTO) {
        try {
            Page<Manufacturer> manufacturerPage = manufacturerService.listAll(pageDTO);

            //Not empty
            if (!manufacturerPage.isEmpty()) {
                return new ResponseEntity<>(manufacturerPage, HttpStatus.OK);
            }

            // No content
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build create manufacturer REST API
     *
     * @param manufacturer This is a manufacturer
     * @return a manufacturer is inserted into database
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createManufacturer(@RequestBody Manufacturer manufacturer) {
        try {

            if (manufacturerService.checkForExistence(manufacturer.getManufacturerName()) == false) {
                manufacturerService.save(manufacturer);

                // Successfully
                body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                return new ResponseEntity<>(body, HttpStatus.CREATED);
            }

            // Duplicate manufacturerName
            body.setResponse(Response.Key.STATUS, Response.Value.DUPLICATED);
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Build get manufacturer by id REST API
     *
     * @param id This is manufacturer id
     * @return a manufacturer
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable long id) {
        try {
            Optional<Manufacturer> manufacturer = manufacturerService.getManufacturerById(id);

            // Found
            if (manufacturer.isPresent()) {
                // Successfully
                return new ResponseEntity<>(manufacturer.get(), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build update manufacturer REST API
     *
     * @param id           This is manufacturer id
     * @param manufacturer This manufacturer details
     * @return manufacturer is updated
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateManufacturer(@PathVariable long id, @RequestBody Manufacturer manufacturer) {
        try {
            Optional<Manufacturer> updateManufacturer = manufacturerService.getManufacturerById(id);

            // Found
            if (updateManufacturer.isPresent()) {

                // Update
                Manufacturer _manufacturer = updateManufacturer.get();
                _manufacturer.setManufacturerName(manufacturer.getManufacturerName());

                // Successful
                return new ResponseEntity<>(manufacturerService.save(_manufacturer), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build delete manufacturer REST API
     *
     * @param id This is manufacturer
     * @return http status
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable long id) {
        try {
            Optional<Manufacturer> updateManufacturer = manufacturerService.getManufacturerById(id);

            // Found
            if (updateManufacturer.isPresent()) {

                // Delete
                manufacturerService.delete(id);

                //Successful
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build search promotion REST API
     *
     * @param keyword This is keyword
     * @return http status
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchManufacturer(@RequestBody PageDTO pageDTO, @RequestParam String keyword) {
        try {
            Page<Manufacturer> manufacturerList = manufacturerService.search(keyword, pageDTO);

            //Not found
            if (manufacturerList.isEmpty()) {
                //No content
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
            }

            //Found
            return new ResponseEntity<>(manufacturerList, HttpStatus.OK);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            //Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
