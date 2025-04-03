package com.example.auction_web.utils.Job;

import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.SessionLog;
import com.example.auction_web.entity.SessionWinner;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.repository.*;
import com.example.auction_web.service.SessionWinnerService;
import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import com.example.auction_web.utils.RabbitMQ.Producer.SessionEndProducer;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionSessionEndTimeJob implements Job {

    @Autowired
    private AuctionSessionRepository auctionSessionRepository;
    @Autowired
    private AuctionHistoryRepository auctionHistoryRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private SessionWinnerRepository sessionWinnerRepository;
    @Autowired
    SessionWinnerService sessionWinnerService;
    @Autowired
    private SessionEndProducer sessionEndProducer;

    private final SessionLogRepository sessionLogRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String auctionSessionId = context.getJobDetail().getJobDataMap().getString("auctionSessionId");
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).get();
        Asset asset = auctionSession.getAsset();

        SessionLog sessionLog = sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.ONGOING.toString());
        List<AuctionHistory> auctionHistory = auctionHistoryRepository.findAuctionHistorysByAuctionSession_AuctionSessionId(auctionSessionId);
        try {
            if (auctionSession != null) {
                if (!auctionHistory.isEmpty()) {
                    auctionSession.setStatus(AUCTION_STATUS.AUCTION_SUCCESS.toString());
                    asset.setStatus(ASSET_STATUS.AUCTION_SUCCESS.toString());

                    AuctionSessionInfoResponse sessionInfo = auctionHistoryRepository.findAuctionSessionInfo(auctionSessionId).get(0);
                    SessionWinnerCreateRequest request = SessionWinnerCreateRequest.builder()
                            .auctionSessionId(auctionSessionId)
                            .userId(sessionInfo.getUserId())
                            .price(sessionInfo.getHighestBid())
                            .victoryTime(auctionSession.getEndTime())
                            .build();

                    sessionWinnerService.createSessionWinner(request);
                    sendMessageToRabbitMQ(auctionSessionId, sessionInfo.getUserId());
                } else {
                    auctionSession.setStatus(AUCTION_STATUS.AUCTION_FAILED.toString());
                    asset.setStatus(ASSET_STATUS.AUCTION_FAILED.toString());
                }
                auctionSessionRepository.save(auctionSession);
                assetRepository.save(asset);
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

    public void sendMessageToRabbitMQ(String auctionSessionId, String AuctionSesstionWinnerId) {
        var sessionEndMessage = SessionEndMessage.builder()
                .AuctionSessionId(auctionSessionId)
                .AuctionSessionWinnerId(AuctionSesstionWinnerId)
                .build();
        sessionEndProducer.sendSessionEndEvent(sessionEndMessage);
    }
}
