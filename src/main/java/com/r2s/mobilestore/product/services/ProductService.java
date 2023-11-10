package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.entities.Product;

import java.io.IOException;

public interface ProductService {
    Product createProduct(CreateProductDTO createProductDTO) throws IOException;
}
