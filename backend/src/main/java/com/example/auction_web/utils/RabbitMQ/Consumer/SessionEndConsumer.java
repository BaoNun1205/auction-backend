package com.example.auction_web.utils.RabbitMQ.Consumer;

import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.repository.BalanceHistoryRepository;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SessionEndConsumer {
    @NonFinal
    @Value("${email.username}")
    private String EMAIL_ADMIN;

    private final DepositRepository depositRepository;
    private final BalanceUserRepository balanceUserRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BalanceHistoryMapper balanceHistoryMapper;

    @RabbitListener(queues = "sessionEndQueue")
    public void processSessionEnd(SessionEndMessage sessionEndMessage) {
        String auctionSessionWinnerId = sessionEndMessage.getAuctionSessionWinnerId();
        String auctionSessionId = sessionEndMessage.getAuctionSessionId();

        var deposits = depositRepository.findActiveDepositsByAuctionSessionIdAndDelFlag(auctionSessionId);

        for (var deposit : deposits) {
            if (!deposit.getUser().getUserId().equals(auctionSessionWinnerId)) {
                balanceUserRepository.increaseBalance(deposit.getUser().getUserId(), deposit.getDepositPrice());
                addBalanceHistory(deposit.getUser().getUserId(), deposit.getDepositPrice(), "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.ADD);
                balanceUserRepository.minusBalance(EMAIL_ADMIN, deposit.getDepositPrice());
                addBalanceHistory(balanceUserRepository.findBalanceUserByUser_Email(EMAIL_ADMIN).getBalanceUserId(), deposit.getDepositPrice(), "Refund for auctionSessionId: " + auctionSessionId, ACTIONBALANCE.SUBTRACT);
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
