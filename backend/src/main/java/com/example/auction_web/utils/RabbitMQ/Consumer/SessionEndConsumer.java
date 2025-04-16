package com.example.auction_web.utils.RabbitMQ.Consumer;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.configuration.ApplicationInitConfig;
import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.BalanceHistoryRepository;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SessionEndConsumer {
    @NonFinal
    @Value("${email.username}")
    private String EMAIL_ADMIN;

    DepositRepository depositRepository;
    BalanceUserRepository balanceUserRepository;
    BalanceHistoryRepository balanceHistoryRepository;
    BalanceHistoryMapper balanceHistoryMapper;
    NotificationStompService notificationStompService;
    AuctionSessionService auctionSessionService;
    UserService userService;

    @RabbitListener(queues = "sessionEndQueue")
    public void processSessionEnd(SessionEndMessage sessionEndMessage) {
        String auctionSessionWinnerId = sessionEndMessage.getAuctionSessionWinnerId();
        String auctionSessionId = sessionEndMessage.getAuctionSessionId();

        var deposits = depositRepository.findActiveDepositsByAuctionSessionIdAndDelFlag(auctionSessionId);
        var auctionSession = auctionSessionService.getAuctionSessionById(auctionSessionId);
        var senderId = userService.getUserByEmail(EMAIL_ADMIN).getUserId();

        for (var deposit : deposits) {
            String userId = deposit.getUser().getUserId();
            if (!userId.equals(auctionSessionWinnerId)) {
                // Cộng tiền lại cho user
                balanceUserRepository.increaseBalance(userId, deposit.getDepositPrice());
                addBalanceHistory(userId, deposit.getDepositPrice(), 
                    "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.ADD);

                // Trừ tiền từ admin
                balanceUserRepository.minusBalance(EMAIL_ADMIN, deposit.getDepositPrice());
                addBalanceHistory(balanceUserRepository.findBalanceUserByUser_Email(EMAIL_ADMIN).getBalanceUserId(),
                    deposit.getDepositPrice(), "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.SUBTRACT);

                // Gửi thông báo hoàn tiền cho người dùng
                NotificationRequest notification = NotificationRequest.builder()
                    .senderId(senderId)
                    .receiverId(userId)
                    .type(NotificationType.REFUND)
                    .title("Hoàn tiền đặt cọc")
                    .content("Bạn đã được hoàn lại " + deposit.getDepositPrice() + " VNĐ cho phiên: " + auctionSession.getName())
                    .referenceId(deposit.getDepositId())
                    .build();

                notificationStompService.sendUserNotification(userId, notification);
            }
        }
    }

    void addBalanceHistory(String BalanceUserId, BigDecimal amount, String Description, ACTIONBALANCE action) {
        BalanceHistoryCreateRequest request = BalanceHistoryCreateRequest.builder()
                .balanceUserId(BalanceUserId)
                .amount(amount)
                .description(Description)
                .actionbalance(action)
                .build();
        balanceHistoryRepository.save(balanceHistoryMapper.toBalanceHistory(request));
    }
}
