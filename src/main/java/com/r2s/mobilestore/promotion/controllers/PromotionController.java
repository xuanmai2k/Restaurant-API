package com.r2s.mobilestore.promotion.controllers;

import com.r2s.mobilestore.category.controllers.CategoryController;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
import com.r2s.mobilestore.promotion.entities.Promotion;
import com.r2s.mobilestore.promotion.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

/**
 * Represents a promotion controller
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@RestController
@RequestMapping("${promotion}")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    private final ResponseDTO body = ResponseDTO.getInstance();

    @Value("${LENGTH_OF_DISCOUNT_CODE_PROMOTION}")
    private Integer length;

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    /**
     * REST API methods for Retrieval operations
     *
     * @param searchPromotionDTO This is discount code
     * @param pageNumber   This is number of page
     * @param pageSize     This is size of page
     * @return list all of promotions
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllPromotions(@RequestBody(required = false) SearchPromotionDTO searchPromotionDTO,
                                              @RequestParam Integer pageNumber,
                                              @RequestParam Integer pageSize) {
        try {
            Page<Promotion> promotionList;
            if (searchPromotionDTO == null) {
                //get All List
                promotionList = promotionService.listAll(pageNumber, pageSize);
            } else {
                //Filter
                promotionList = promotionService.search(searchPromotionDTO, pageNumber, pageSize);
            }

            //Not empty
            if (!promotionList.isEmpty()) {
                return new ResponseEntity<>(promotionList, HttpStatus.OK);
            }

            // No content
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build get promotion by id REST API
     *
     * @param id This is promotion id
     * @return a promotion
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable Long id) {
        try {
            Optional<Promotion> promotion = promotionService.getPromotionById(id);

            //Found
            if (promotion.isPresent()) {
                // Successfully
                return new ResponseEntity<>(promotion.get(), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build create promotion REST API
     *
     * @param promotion This is a promotion
     * @return a promotion is inserted into database
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> createPromotion(@RequestBody Promotion promotion) {
        try {
            //random discount code
            String discountCode = promotionService.getRandomDiscountCode(length);
            promotion.setDiscountCode(discountCode);
            promotionService.save(promotion);

            // Successfully
            body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build update promotion REST API
     *
     * @param id        This is promotion id
     * @param promotion This promotion details
     * @return promotion is updated
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updatePromotion(@RequestBody Promotion promotion, @PathVariable Long id) {
        try {
            Optional<Promotion> updatePromotion = promotionService.getPromotionById(id);

            //Found
            if (updatePromotion.isPresent()) {
                Promotion _promotion = updatePromotion.get();
                _promotion.setTotalPurchase(promotion.getTotalPurchase());
                _promotion.setDiscount(promotion.getDiscount());
                _promotion.setMaxPromotionGet(promotion.getMaxPromotionGet());
                _promotion.setExpireDate(promotion.getExpireDate());
                _promotion.setCampaignDescription(promotion.getCampaignDescription());
                _promotion.setDiscountAvailable(promotion.getDiscountAvailable());

                //Successfully
                return new ResponseEntity<>(promotionService.save(_promotion), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build delete promotion REST API
     *
     * @param id This is promotion
     * @return http status
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        try {
            Optional<Promotion> promotion = promotionService.getPromotionById(id);

            //Not Found
            if (!promotion.isPresent()) {
                return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
            }

            //Found
            promotionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
