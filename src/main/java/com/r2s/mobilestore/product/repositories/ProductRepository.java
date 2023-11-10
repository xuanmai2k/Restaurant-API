package com.r2s.mobilestore.product.repositories;

import com.r2s.mobilestore.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
