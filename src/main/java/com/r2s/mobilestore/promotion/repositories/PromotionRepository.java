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

    @Query("SELECT p " +
            "FROM Promotion p " +
            "WHERE p.discountCode LIKE %:discountCode% " +
            "AND p.expireDate >= :minExpireDate " +
            "AND p.expireDate <= :maxExpireDate " +
            "AND p.discountAvailable = :discountAvailable " +
            "AND p.discount >= :minDiscount " +
            "AND p.discount <= :maxDiscount")
    Page<Promotion> searchPromotion(@Param("discountCode") String discountCode,
                                    @Param("minExpireDate") LocalDate minExpireDate,
                                    @Param("maxExpireDate") LocalDate maxExpireDate,
                                    @Param("discountAvailable") Boolean discountAvailable,
                                    @Param("minDiscount") Integer minDiscount,
                                    @Param("maxDiscount") Integer maxDiscount,
                                    Pageable pageable);

    @Query("SELECT p " +
            "FROM Promotion p " +
            "WHERE p.discountCode LIKE %:discountCode% " +
            "AND p.expireDate >= :minExpireDate " +
            "AND p.expireDate <= :maxExpireDate " +
            "AND p.discount >= :minDiscount " +
            "AND p.discount <= :maxDiscount")
    Page<Promotion> searchPromotionWithoutDiscountAvailable(@Param("discountCode") String discountCode,
                                                            @Param("minExpireDate") LocalDate minExpireDate,
                                                            @Param("maxExpireDate") LocalDate maxExpireDate,
                                                            @Param("minDiscount") Integer minDiscount,
                                                            @Param("maxDiscount") Integer maxDiscount,
                                                            Pageable pageable);

    @Query("SELECT MIN( p.expireDate ) FROM Promotion p ")
    LocalDate findMinExpireDate();

    @Query("SELECT MAX( p.expireDate ) FROM Promotion p")
    LocalDate findMaxExpireDate();

    boolean existsByDiscountCode(String discountCode);
}
