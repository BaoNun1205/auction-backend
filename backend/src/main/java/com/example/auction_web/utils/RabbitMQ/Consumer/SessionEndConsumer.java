package com.example.auction_web.utils.RabbitMQ.Consumer;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.configuration.ApplicationInitConfig;
import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.BalanceHistoryRepository;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.BalanceUserService;
import com.example.auction_web.service.DepositService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import jakarta.transaction.Transactional;
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
    BalanceUserService balanceUserService;
    DepositService depositService;
    UserService userService;

    @Transactional
    @RabbitListener(queues = "sessionEndQueue")
    public void processSessionEnd(SessionEndMessage sessionEndMessage) {
        String auctionSessionWinnerId = sessionEndMessage.getAuctionSessionWinnerId();
        String auctionSessionId = sessionEndMessage.getAuctionSessionId();

        var deposits = depositService.findDepositByAuctionSessionId(auctionSessionId);
        var auctionSession = auctionSessionService.getAuctionSessionById(auctionSessionId);
        var senderId = userService.getUserByEmail(EMAIL_ADMIN).getUserId();
        var balanceUserAdmin = balanceUserService.getBalanceUserAdmin();

        for (var deposit : deposits) {
            String userId = deposit.getUserId();
            var balanUser = balanceUserService.getCoinUserByUserId(userId);
            if (!userId.equals(auctionSessionWinnerId)) {
                balanceUserRepository.increaseBalance(userId, deposit.getDepositPrice());
                addBalanceHistory(balanUser, deposit.getDepositPrice(),
                    "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.ADD);

                balanceUserRepository.minusBalance(EMAIL_ADMIN, deposit.getDepositPrice());
                addBalanceHistory(balanceUserAdmin,
                    deposit.getDepositPrice(), "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.SUBTRACT);

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

    void addBalanceHistory(BalanceUserResponse BalanceUser, BigDecimal amount, String Description, ACTIONBALANCE action) {
        var balanceUser = balanceUserRepository.findById(BalanceUser.getBalanceUserId())
                .orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        BalanceHistoryCreateRequest request = BalanceHistoryCreateRequest.builder()
                .amount(amount)
                .description(Description)
                .actionbalance(action)
                .build();
        var balanceHistory = balanceHistoryMapper.toBalanceHistory(request);
        balanceHistory.setBalanceUser(balanceUser);
        balanceHistoryRepository.save(balanceHistory);
    }
}
