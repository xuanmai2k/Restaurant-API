package com.r2s.mobilestore.product.repositories;

import com.r2s.mobilestore.product.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Represents a product repository
 * all crud database methods
 *
 * @author xuanmai
 * @since 2023-10-06
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductsById(Long id);

    @Query("SELECT p , c.categoryName, m.manufacturerName " +
            "FROM Product p " +
            "INNER JOIN Category c ON p.category.id = c.id " +
            "INNER JOIN Manufacturer m ON p.manufacturer.id = m.id " +
            "WHERE p.name LIKE %:keyword% " +
            "OR p.productCode LIKE %:keyword% " +
            "AND m.manufacturerName LIKE %:manufacturer% " +
            "AND c.categoryName LIKE %:category%" )
    Page<Product> searchProduct(@Param("keyword") String keyword,
                                @Param("manufacturer") String manufacturer,
                                @Param("category") String category,
                                Pageable pageable);


    boolean existsByProductCode(String productCode);
}
