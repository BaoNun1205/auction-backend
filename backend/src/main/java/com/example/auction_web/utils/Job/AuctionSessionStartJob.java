package com.example.auction_web.utils.Job;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.SessionLog;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.SessionLogRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuctionSessionStartJob implements Job {

    @Autowired
    private AuctionSessionRepository auctionSessionRepository;

    private final SessionLogRepository sessionLogRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String auctionSessionId = context.getJobDetail().getJobDataMap().getString("auctionSessionId");
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).get();
        Asset asset = auctionSession.getAsset();

        SessionLog sessionLog = sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.UPCOMING.toString());
        try {
            if (auctionSession != null) {
                auctionSession.setStatus(AUCTION_STATUS.ONGOING.toString());
                asset.setStatus(ASSET_STATUS.ONGOING.toString());
                assetRepository.save(asset);
                auctionSessionRepository.save(auctionSession);
            }

            if (sessionLog != null) {
                sessionLog.setSentTime(LocalDateTime.now());
                sessionLog.setStatus(SessionLog.SessionLogStatus.SENT);
                sessionLogRepository.save(sessionLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sessionLog != null) {
                sessionLog.setStatus(SessionLog.SessionLogStatus.FAILED);
                sessionLogRepository.save(sessionLog);
            }
        }
    }
}
