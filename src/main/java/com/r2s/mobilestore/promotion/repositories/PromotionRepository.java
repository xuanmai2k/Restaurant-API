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
            "AND (:customerGroup = '' OR p.customerGroup LIKE %:customerGroup% ) " +
            "AND (p.status LIKE %:status% OR :status IS NULL ) " +
            "AND ((" +
            "(:isBeforeManufactureDate = true AND (p.manufactureDate <= :manufactureDate OR :manufactureDate IS NULL)) " +
            "OR (:isBeforeManufactureDate = false AND (p.manufactureDate >= :manufactureDate OR :manufactureDate IS NULL))) " +
            "OR (:isBeforeManufactureDate IS NULL AND :manufactureDate IS NULL)) " +
            "AND ((" +
            ":compareUsed = 'equal' AND (p.used = :used OR :used IS NULL)) " +
            "OR (:compareUsed = 'less' AND (p.used <= :used OR :used IS NULL)) " +
            "OR (:compareUsed = 'more' AND (p.used >= :used OR :used IS NULL)) " +
            "OR (:compareUsed = 'other' AND (p.used != :used OR :used IS NULL)) " +
            "OR (:compareUsed IS NULL AND :used IS NULL)) ")
    Page<Promotion> searchPromotion(@Param("discountCode") String discountCode,
                                    @Param("customerGroup") String customerGroup,
                                    @Param("status") String status,
                                    @Param("isBeforeManufactureDate") Boolean isBeforeManufactureDate,
                                    @Param("manufactureDate") LocalDate manufactureDate,
                                    @Param("compareUsed") String compareUsed,
                                    @Param("used") Integer used,
                                    Pageable pageable);

    @Query("SELECT p FROM Promotion p " +
            "WHERE " +
            "(:status = '' OR p.status LIKE %:status% )")
    Page<Promotion> searchPromotionByStatus(@Param("status") String status,
                                            Pageable pageable);

    boolean existsByDiscountCode(String discountCode);
}
