package com.r2s.mobilestore.promotion.repositories;

import com.r2s.mobilestore.promotion.entities.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Represents a promotion repository
 * all crud database methods
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("SELECT p FROM Promotion p " +
            "WHERE " +
            "(:discountCode = '' OR p.discountCode LIKE %:discountCode% ) " +
            "AND (p.discountAvailable = :discountAvailable OR :discountAvailable IS NULL) " +
            "AND (p.expireDate = :expireDate OR :expireDate IS NULL) " +
            "AND (p.discount >= :minDiscount OR :minDiscount IS NULL) " +
            "AND (p.discount <= :maxDiscount OR :maxDiscount IS NULL)")
    Page<Promotion> searchPromotion(@Param("discountCode") String discountCode,
                                    @Param("expireDate") LocalDate expireDate,
                                    @Param("discountAvailable") Boolean discountAvailable,
                                    @Param("minDiscount") Integer minDiscount,
                                    @Param("maxDiscount") Integer maxDiscount,
                                    Pageable pageable);

    boolean existsByDiscountCode(String discountCode);
}
