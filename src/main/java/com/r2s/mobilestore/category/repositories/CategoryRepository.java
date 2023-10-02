package com.r2s.mobilestore.category.repositories;

import com.r2s.mobilestore.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Represents a category repository
 * all crud database methods
 *
 * @author kyle
 * @since 2023-08-31
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryNameContaining(String categoryName);
}
