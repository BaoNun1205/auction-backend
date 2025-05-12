package com.example.auction_web.utils.Job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.auction_web.personalization.service.impl.RecommendServiceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecommendScheduler {
    RecommendServiceImpl recommendService;

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at 1 AM
    public void scheduleUserVectorUpdate() {
        recommendService.batchUpdateUserVectors();
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at 1:30 AM
    public void scheduleAuctionSessionVectorUpdate() {
        recommendService.batchUpdateAuctionSessionVectors();
    }
}
