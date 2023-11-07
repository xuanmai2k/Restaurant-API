package com.r2s.mobilestore.utils;

import com.r2s.mobilestore.promotion.service.PromotionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private PromotionService promotionService;

    /**
     * Get all promotions which expireDay is today to change the status
     */
    @PostConstruct
    @Scheduled(cron = "59 59 23 * * *")  // Run at 23h59m59s everyday
    public void updateExpireStatus() {
        System.out.println("update expire promotion status");
        promotionService.updateExpirePromotionStatus();
    }

    /**
     * Get all promotions which manufactureDay is today to change the status
     */
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * *")  // Run at 0h everyday
    public void updateActivateStatus() {
        System.out.println("update activate promotion status");
        promotionService.updateActivatePromotionStatus();
    }

}
