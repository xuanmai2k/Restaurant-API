package com.r2s.mobilestore.promotion.service;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
import com.r2s.mobilestore.promotion.entities.Promotion;
import com.r2s.mobilestore.promotion.repositories.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Represents a promotion service
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@Service
@Transactional
@Component
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Value("${ALLOWED_CHARACTERS}")
    private String ALLOWED_CHARACTERS;

    @Value("${ACTIVATE}")
    private String ACTIVATE;

    @Value("${EXPIRE}")
    private String EXPIRE;

    /**
     * This method is used to list promotions follow by status
     *
     * @param status  This is a status of promotion
     * @param pageDTO This is a page
     * @return list all of promotions
     */
    @Override
    public Page<Promotion> listFollowByStatus(String status, PageDTO pageDTO) {
        return promotionRepository.searchPromotionByStatus(status, PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
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
     * @param length This is the length of promotion
     */
    @Override
    public String getRandomDiscountCode(Integer length) {
        String discountCode;

        //create discount code
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        do {
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
                sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
            }
            discountCode = sb.toString();

        } while (checkForExistence(discountCode)); // check discount code

        return discountCode;
    }

    /**
     * This method is used to check discount code
     *
     * @param discountCode This is discount code
     */
    @Override
    public Boolean checkForExistence(String discountCode) {
        return promotionRepository.existsByDiscountCode(discountCode);
    }

    /**
     * Get all promotions containing discount code
     *
     * @param searchPromotionDTO This is keyword
     * @return List of promotions
     */
    @Override
    public Page<Promotion> search(SearchPromotionDTO searchPromotionDTO) {
        return promotionRepository.searchPromotion(
                searchPromotionDTO.getDiscountCode(),
                searchPromotionDTO.getCustomerGroup(),
                searchPromotionDTO.getStatus(),
                searchPromotionDTO.getIsBeforeManufactureDate(),
                searchPromotionDTO.getManufactureDate(),
                searchPromotionDTO.getCompareUsed(),
                searchPromotionDTO.getUsed(),
                PageRequest.of(searchPromotionDTO.getPageDTO().getPageNumber(), searchPromotionDTO.getPageDTO().getPageSize()));
    }

    /**
     * Get all promotions which manufactureDay is today to change the status
     */
    @Override
    public void updateActivatePromotionStatus() {
        LocalDate currentDate = LocalDate.now();

        List<Promotion> promotions = promotionRepository.findByManufactureDate(currentDate);

        for (Promotion promotion : promotions) {
            promotion.setStatus(ACTIVATE);
            save(promotion);
        }
    }

    /**
     * Get all promotions which expireDay is today to change the status
     */
    @Override
    public void updateExpirePromotionStatus() {
        LocalDate currentDate = LocalDate.now();

        List<Promotion> promotions = promotionRepository.findByExpireDate(currentDate);

        for (Promotion promotion : promotions) {
            promotion.setStatus(EXPIRE);
            save(promotion);
        }
    }

}
