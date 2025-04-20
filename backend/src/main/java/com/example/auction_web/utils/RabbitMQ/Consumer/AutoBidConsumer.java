package com.example.auction_web.utils.RabbitMQ.Consumer;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.enums.AUTOBID;
import com.example.auction_web.repository.AutoBidRepository;
import com.example.auction_web.service.AuctionHistoryService;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.utils.RabbitMQ.Dto.BidMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AutoBidConsumer {
    private final AutoBidRepository autoBidRepository;
    private final AuctionHistoryService auctionHistoryService;
    private final AuctionSessionService auctionSessionService;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "bidQueue")
    public void processAutoBid(BidMessage bidMessage) {
        String auctionSessionId = bidMessage.getAuctionSessionId();
        BigDecimal currentPrice = bidMessage.getCurrentPrice();

        var autoBids = autoBidRepository.findAutoBidsByAuctionSession_AuctionSessionIdAndStatus(auctionSessionId, AUTOBID.ACTIVE);

        for (var autoBidItem : autoBids) {
            if (currentPrice.add(autoBidItem.getBidIncrement()).compareTo(autoBidItem.getMaxBidPrice()) > 0) {
                autoBidItem.setStatus(AUTOBID.INACTIVE);
                autoBidRepository.save(autoBidItem);
            }
            else {
                AuctionHistoryCreateRequest auctionHistoryCreateRequest = AuctionHistoryCreateRequest.builder()
                        .auctionSessionId(auctionSessionId)
                        .userId(autoBidItem.getUser().getUserId())
                        .bidPrice(currentPrice.add(autoBidItem.getBidIncrement()))
                        .bidTime(LocalDateTime.now())
                        .build();
                try {
                    auctionHistoryService.createAuctionHistory(auctionHistoryCreateRequest);
                    AuctionSessionInfoDetail auctionSessionInfoDetail = auctionSessionService.getDetailAuctionSessionById(auctionSessionId);

                    Thread.sleep(150);

                    Map<String, Object> response = new HashMap<>();
                    response.put("code", 200);
                    response.put("result", auctionSessionInfoDetail);

                    messagingTemplate.convertAndSend(
                            "/rt-product/bidPrice-update/" + auctionSessionId,
                            response
                    );
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
