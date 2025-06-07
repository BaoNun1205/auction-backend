package com.example.auction_web.utils.Job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.auction_web.personalization.service.RecommendService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecommendScheduler {
    RecommendService recommendService;

    @Scheduled(cron = "0 18 0 * * ?")
    public void scheduleUserVectorUpdate() {
        recommendService.batchUpdateUserVectors();
    }

    // @Scheduled(cron = "0 18 0 * * ?")
    // public void scheduleAuctionSessionVectorUpdate() {
    //     recommendService.batchUpdateAuctionSessionVectors();
    // }
}
