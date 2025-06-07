package com.example.auction_web.utils.Quataz;

import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.entity.ScheduleLog.PaymentCancelLog;
import com.example.auction_web.repository.PaymentCancelRepository;
import com.example.auction_web.service.BalanceHistoryService;
import com.example.auction_web.utils.Job.PaymentCancelJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentCancelService {
    private final Scheduler scheduler;
    private final PaymentCancelRepository paymentCancelRepository;
    private final BalanceHistoryService balanceHistoryService;

    public void setSchedulerPaymentCancel(String sellerId, String auctionSessionId, LocalDateTime notificationTime) {
        JobDetail jobDetail = JobBuilder.newJob(PaymentCancelJob.class)
                .withIdentity("sellerIdJob-" + auctionSessionId, "paymentCancelGroup")
                .usingJobData("sellerId", sellerId)
                .usingJobData("auctionSessionId", auctionSessionId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("sellerIdTrigger-" + auctionSessionId, "paymentCancelGroup")
                .startAt(Date.from(notificationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        JobKey jobKey = new JobKey("sellerIdJob-" + auctionSessionId, "paymentCancelGroup");
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }

            scheduler.scheduleJob(jobDetail, trigger);

            if (paymentCancelRepository.findPaymentCancelLogByAuctionSessionIdAndSellerId(sellerId, auctionSessionId) == null) {
                PaymentCancelLog paymentCancelLog = new PaymentCancelLog();
                paymentCancelLog.setAuctionSessionId(auctionSessionId);
                paymentCancelLog.setSellerId(sellerId);
                paymentCancelLog.setScheduledTime(notificationTime);
                paymentCancelLog.setStatus(NotificationLog.NotificationStatus.SCHEDULED);
                paymentCancelRepository.save(paymentCancelLog);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void reschedulePendingNotifications() {
        List<PaymentCancelLog> pendingNotifications = paymentCancelRepository.findByStatus(NotificationLog.NotificationStatus.SCHEDULED);
        pendingNotifications.forEach(this::rescheduleNotification);
    }

    private void rescheduleNotification(PaymentCancelLog paymentCancelLog) {
        LocalDateTime notificationTime = paymentCancelLog.getScheduledTime();
        String auctionSessionId = paymentCancelLog.getAuctionSessionId();
        String sellerId = paymentCancelLog.getSellerId();

        if (notificationTime.isAfter(LocalDateTime.now()) && paymentCancelLog.getStatus() == NotificationLog.NotificationStatus.SCHEDULED) {
            setSchedulerPaymentCancel(sellerId, auctionSessionId, notificationTime);
        } else if (notificationTime.isBefore(LocalDateTime.now()) && paymentCancelLog.getStatus() == NotificationLog.NotificationStatus.SCHEDULED) {
            paymentCancelLog.setStatus(NotificationLog.NotificationStatus.SENT);
            paymentCancelRepository.save(paymentCancelLog);

            balanceHistoryService.cancelSession(sellerId, auctionSessionId);
        } else {
            paymentCancelLog.setStatus(NotificationLog.NotificationStatus.FAILED);
            paymentCancelRepository.save(paymentCancelLog);
        }
    }
}
