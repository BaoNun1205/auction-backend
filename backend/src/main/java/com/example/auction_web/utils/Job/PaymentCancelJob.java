package com.example.auction_web.utils.Job;

import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.repository.PaymentCancelRepository;
import com.example.auction_web.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PaymentCancelJob implements Job {
    PaymentCancelRepository paymentCancelRepository;
    BalanceHistoryService balanceHistoryService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String sellerId = jobExecutionContext.getJobDetail().getJobDataMap().getString("sellerId");
        String auctionSessionId = jobExecutionContext.getJobDetail().getJobDataMap().getString("auctionSessionId");

        var paymentCanCelLog = paymentCancelRepository.findPaymentCancelLogByAuctionSessionIdAndSellerId(auctionSessionId, sellerId);

        try {
            balanceHistoryService.cancelSession(sellerId, auctionSessionId);

            if (paymentCanCelLog != null) {
                paymentCanCelLog.setStatus(NotificationLog.NotificationStatus.SENT);
                paymentCanCelLog.setSentTime(LocalDateTime.now());
                paymentCancelRepository.save(paymentCanCelLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            paymentCanCelLog.setStatus(NotificationLog.NotificationStatus.FAILED);
            paymentCancelRepository.save(paymentCanCelLog);
        }
    }
}
