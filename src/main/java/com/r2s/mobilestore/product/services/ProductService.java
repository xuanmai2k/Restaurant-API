package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.entities.Property;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public interface ProductService {
    ArrayList<Property> createProduct(CreateProductDTO createProductDTO) throws IOException;

    String getRandomProductCode(int length);

    Page<Product> getAllProducts(PageDTO pageDTO);

    Optional<Product> getProductById(Long id);
}
