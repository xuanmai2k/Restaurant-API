package com.r2s.mobilestore.promotion.controllers;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
import com.r2s.mobilestore.promotion.entities.Promotion;
import com.r2s.mobilestore.promotion.service.PromotionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private Integer LENGTH;

    @Value("${ACTIVATE}")
    private String ACTIVATE;

    @Value("${NOT_ACTIVATE}")
    private String NOT_ACTIVATE;

    @Value("${PENDING}")
    private String PENDING;

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(PromotionController.class);

    /**
     * REST API methods for Retrieval operations
     *
     * @param pageDTO This is a page
     * @param status  This is a status of promotions
     * @return list all of promotions
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllPromotions(@RequestBody PageDTO pageDTO, @RequestParam String status) {
        try {
            Page<Promotion> promotionList = promotionService.listFollowByStatus(status, pageDTO);

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
     * Build generate discount code of promotion REST API
     *
     * @return a discount code automatically
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/generate-code")
    public ResponseEntity<?> generateCode() {
        try {
            //random discount code
            String discountCode = promotionService.getRandomDiscountCode(LENGTH);

            // Successfully
            return new ResponseEntity<>(discountCode, HttpStatus.OK);
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
     * @return http status
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> createPromotion(@RequestBody @Valid Promotion promotion) {
        try {
            LocalDate now = LocalDate.now();
            // Check discount code existence
            if (promotionService.checkForExistence(promotion.getDiscountCode()) == false) {

                // Check manufacture date <= expire date
                if (promotion.getManufactureDate().isBefore(promotion.getExpireDate())
                        || promotion.getManufactureDate().isEqual(promotion.getExpireDate())) {

                    //Set status
                    LocalDate currentDate = LocalDate.now();
                    if (promotion.getManufactureDate().isEqual(currentDate)) {
                        promotion.setStatus(ACTIVATE);
                    } else {
                        promotion.setStatus(NOT_ACTIVATE);
                    }

                    // Save
                    promotionService.save(promotion);

                    // Successfully
                    body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                    return new ResponseEntity<>(body, HttpStatus.CREATED);
                }

                // Invalid value manufacture and expire date
                body.setResponse(Response.Key.STATUS, Response.Value.INVALID_VALUE);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }

            // Duplicate discount code
            body.setResponse(Response.Key.STATUS, Response.Value.DUPLICATED);
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build change status promotion
     *
     * @param id This is a promotion id
     * @return updated status of promotion
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatusPromotion(@PathVariable Long id) {
        try {
            Optional<Promotion> updatePromotion = promotionService.getPromotionById(id);

            //Found
            if (updatePromotion.isPresent()) {

                //Check status activated
                if (updatePromotion.get().getStatus().equals(ACTIVATE)) {
                    Promotion _promotion = updatePromotion.get();
                    _promotion.setStatus(PENDING);

                    //Successfully
                    body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                    return new ResponseEntity<>(promotionService.save(_promotion), HttpStatus.OK);
                }

                //Check status pending
                if (updatePromotion.get().getStatus().equals(PENDING)) {
                    Promotion _promotion = updatePromotion.get();
                    _promotion.setStatus(ACTIVATE);

                    //Successfully
                    body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                    return new ResponseEntity<>(promotionService.save(_promotion), HttpStatus.OK);
                }

                //Check status not activate
                body.setResponse(Response.Key.STATUS, Response.Value.INVALID_VALUE);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
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
     * Build get promotion by id REST API
     *
     * @param id This is promotion id
     * @return a promotion
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
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
     * Build update promotion REST API
     *
     * @param promotion This promotion details
     * @param id        This is promotion id
     * @return promotion is updated
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updatePromotion(@RequestBody @Valid Promotion promotion, @PathVariable Long id) {
        try {
            Optional<Promotion> updatePromotion = promotionService.getPromotionById(id);

            //Found
            if (updatePromotion.isPresent()) {

                // Check manufacture date <= expire date
                if (promotion.getManufactureDate().isBefore(promotion.getExpireDate())
                        || promotion.getManufactureDate().isEqual(promotion.getExpireDate())) {

                    Promotion _promotion = updatePromotion.get();
                    _promotion.setCampaignDescription(promotion.getCampaignDescription());
                    _promotion.setManufactureDate(promotion.getManufactureDate());
                    _promotion.setExpireDate(promotion.getExpireDate());
                    _promotion.setPercentageDiscount(promotion.getPercentageDiscount());
                    _promotion.setMaximumPriceDiscount(promotion.getMaximumPriceDiscount());
                    _promotion.setMinimumOrderValue(promotion.getMinimumOrderValue());
                    _promotion.setCustomerGroup(promotion.getCustomerGroup());

                    //Successfully
                    return new ResponseEntity<>(promotionService.save(_promotion), HttpStatus.OK);
                }

                // Invalid value manufacture and expire date
                body.setResponse(Response.Key.STATUS, Response.Value.INVALID_VALUE);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
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

    /**
     * Build search promotion REST API
     *
     * @param searchPromotionDTO This is keyword
     * @return http status
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchPromotion(@RequestBody SearchPromotionDTO searchPromotionDTO) {
        try {
            Page<Promotion> promotionList = promotionService.search(searchPromotionDTO);

            //Not found
            if (promotionList.isEmpty()) {
                //No content
                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
            }

            //Found
            return new ResponseEntity<>(promotionList, HttpStatus.OK);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            //Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
