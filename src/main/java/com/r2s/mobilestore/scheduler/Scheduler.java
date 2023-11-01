package com.r2s.mobilestore.scheduler;

import com.r2s.mobilestore.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private PromotionService promotionService;


    @Scheduled(cron = "0 0 0 * * *")  // Chạy vào 0h mỗi ngày
    public void updateActivateStatus() {
        System.out.println("start");
        promotionService.updateStartPromotionStatus();
    }

}
