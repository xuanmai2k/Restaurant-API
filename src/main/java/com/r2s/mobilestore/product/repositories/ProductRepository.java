package com.r2s.mobilestore.product.repositories;

import com.r2s.mobilestore.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductsById(Long id);
}
