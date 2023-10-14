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

    @Query("SELECT p " +
            "FROM Product p " +
            "INNER JOIN Category c ON p.category.id = c.id " +
            "WHERE p.name LIKE %:name% " +
            "AND p.manufacturer LIKE %:manufacturer% " +
            "AND c.id = :category" )
    Page<Product> searchProduct(@Param("name") String name,
                                @Param("manufacturer") String manufacturer,
                                @Param("category") String category,
                                Pageable pageable);

    boolean existsByProductCode(String productCode);
}
