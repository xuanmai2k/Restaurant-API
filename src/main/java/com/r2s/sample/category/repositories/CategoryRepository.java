package com.r2s.sample.category.repositories;

import com.r2s.sample.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents a category repository
 * all crud database methods
 *
 * @author kyle
 * @since 2023-08-31
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
