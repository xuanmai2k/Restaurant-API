package com.r2s.mobilestore.promotion.repositories;

import com.r2s.mobilestore.promotion.entities.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Represents a promotion repository
 * all crud database methods
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findByDiscountCodeContaining(String discountCode, Pageable pageable);

    boolean existsByDiscountCode(String discountCode);
}
