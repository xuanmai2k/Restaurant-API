package com.r2s.mobilestore.promotion.service;

import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
import com.r2s.mobilestore.promotion.entities.Promotion;
import com.r2s.mobilestore.promotion.repositories.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a promotion service
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Value("${ALLOWED_CHARACTERS}")
    private String allowedCharacters;

    /**
     * This method is used to list all promotions
     *
     * @param pageNumber This is number of page
     * @param pageSize   This is size of page
     * @return list all of promotions
     */
    @Override
    public Page<Promotion> listAll(int pageNumber, int pageSize) {
        return promotionRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    /**
     * Get all promotions containing discount code
     *
     * @param searchPromotionDTO This is discount code of promotion
     * @param pageNumber         This is number of page
     * @param pageSize           This is size of page
     * @return List of promotions
     */
    @Override
    public Page<Promotion> search(SearchPromotionDTO searchPromotionDTO, int pageNumber, int pageSize) {
        //get max and min of expire date
        LocalDate minExpireDate = promotionRepository.findMinExpireDate();
        LocalDate maxExpireDate = promotionRepository.findMaxExpireDate();

        LocalDate date = null;

        //expire date is not null
        if (searchPromotionDTO.getExpireDate() != null) {
            date = LocalDate.parse(searchPromotionDTO.getExpireDate());
        }

        //discount available is not null
        if (searchPromotionDTO.getDiscountAvailable() != null) {
            return promotionRepository.searchPromotion(
                    searchPromotionDTO.getDiscountCode(),
                    date == null ? minExpireDate : date, //if date is null, get min value
                    date == null ? maxExpireDate : date, //if date is null, get max value
                    searchPromotionDTO.getDiscountAvailable(),
                    searchPromotionDTO.getMinDiscount(),
                    searchPromotionDTO.getMaxDiscount(),
                    PageRequest.of(pageNumber, pageSize));
        } else {

            //discount available is null
            return promotionRepository.searchPromotionWithoutDiscountAvailable(
                    searchPromotionDTO.getDiscountCode(),
                    date == null ? minExpireDate : date,
                    date == null ? maxExpireDate : date,
                    searchPromotionDTO.getMinDiscount(),
                    searchPromotionDTO.getMaxDiscount(),
                    PageRequest.of(pageNumber, pageSize));
        }
    }

    /**
     * This method is used to get a promotion base on id
     *
     * @param id This is promotion id
     * @return promotion base on id
     */
    @Override
    public Optional<Promotion> getPromotionById(Long id) {
        return promotionRepository.findById(id);
    }

    /**
     * This method is used to create a promotion
     *
     * @param promotion This is a promotion
     */
    @Override
    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    /**
     * This method is used to delete a promotion by id
     *
     * @param id This is promotion id
     */
    @Override
    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }

    /**
     * This method is used to random discount code
     *
     * @param length This is promotion id
     */
    @Override
    public String getRandomDiscountCode(Integer length) {
        String discountCode;

        //create discount code
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        do {
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(allowedCharacters.length());
                sb.append(allowedCharacters.charAt(randomIndex));
            }
            discountCode = sb.toString();

        } while (checkForExistence(discountCode)); // check discount code

        return discountCode;
    }

    public Boolean checkForExistence(String discountCode) {
        return promotionRepository.existsByDiscountCode(discountCode);
    }
}
