package com.r2s.mobilestore.product.controllers;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.dtos.CreatePropertyDetailDTO;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.entities.Property;
import com.r2s.mobilestore.product.repositories.PropertyRepository;
import com.r2s.mobilestore.product.services.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("${product}")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyRepository propertyRepository;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid CreateProductDTO createProductDTO) throws IOException {
        try {
            ArrayList<Property> product = productService.createProduct(createProductDTO);

            //Successfully
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            //Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/property")
    public ResponseEntity<?> createPropertyDetail(@RequestBody CreatePropertyDetailDTO createPropertyDetailDTO) throws IOException {
        try {

            for (Property item : createPropertyDetailDTO.getPropertyList()){
                Optional<Property> property = propertyRepository.findById(item.getId());

                if (property.isPresent()){
                    productService.updateProperty(item);
                }else{
                    continue;
                }
            }

            //Successfully
            body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            //Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping()
    public ResponseEntity<?> getAllProducts(@RequestBody PageDTO pageDTO) {
        try {
            Page<Product> productList = productService.getAllProducts(pageDTO);

            //Not found
            if (productList.isEmpty()) {

                //No content
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
            }

            //Found
            return new ResponseEntity<>(productList, HttpStatus.OK);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            //Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);

            //Found
            if (product.isPresent()) {
                //Successfully
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            }

            //Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
