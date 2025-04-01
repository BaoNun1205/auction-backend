package com.example.auction_web.utils.RabbitMQ.Consumer;

import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEndConsumer {
    @NonFinal
    @Value("${email.username}")
    private String EMAIL_ADMIN;

    private final DepositRepository depositRepository;
    private final BalanceUserRepository balanceUserRepository;

    @RabbitListener(queues = "sessionEndQueue")
    public void processSessionEnd(SessionEndMessage sessionEndMessage) {
        String auctionSessionWinnerId = sessionEndMessage.getAuctionSessionWinnerId();
        String auctionSessionId = sessionEndMessage.getAuctionSessionId();

        var deposits = depositRepository.findActiveDepositsByAuctionSessionIdAndDelFlag(auctionSessionId);

        for (var deposit : deposits) {
            if (!deposit.getUser().getUserId().equals(auctionSessionWinnerId)) {
                balanceUserRepository.increaseBalance(deposit.getUser().getUserId(), deposit.getDepositPrice());
                balanceUserRepository.minusBalance(EMAIL_ADMIN, deposit.getDepositPrice());
            }
        }
    }
}
