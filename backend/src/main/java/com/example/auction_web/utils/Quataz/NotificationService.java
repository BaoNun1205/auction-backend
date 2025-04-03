package com.example.auction_web.utils.Quataz;

import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.NotificationRepository;
import com.example.auction_web.utils.Job.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Scheduler scheduler;
    private final JavaMailSender javaMailSender;

    private final NotificationRepository notificationLogRepository;
    private final AuctionSessionRepository auctionSessionRepository;

    public void setSchedulerNotification(String email, String auctionSessionId,LocalDateTime notificationTime) {
        JobDetail jobDetail = JobBuilder.newJob(EmailNotification.class)
                .withIdentity("emailJob-" + auctionSessionId, "notificationGroup")
                .usingJobData("email", email)
                .usingJobData("auctionSessionId", auctionSessionId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("emailTrigger-" + auctionSessionId, "notificationGroup")
                .startAt(Date.from(notificationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);

            if (notificationLogRepository.findNotificationLogByAuctionSessionIdAndEmail(auctionSessionId, email) == null) {
                NotificationLog notificationLog = new NotificationLog();
                notificationLog.setEmail(email);
                notificationLog.setAuctionSessionId(auctionSessionId);
                notificationLog.setScheduledTime(notificationTime);
                notificationLog.setStatus(NotificationLog.NotificationStatus.SCHEDULED);
                notificationLogRepository.save(notificationLog);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void reschedulePendingNotifications() {
        List<NotificationLog> pendingNotifications = notificationLogRepository.findByStatus(NotificationLog.NotificationStatus.SCHEDULED);
        pendingNotifications.forEach(this::rescheduleNotification);
    }

    private void rescheduleNotification(NotificationLog notificationLog) {
        LocalDateTime notificationTime = notificationLog.getScheduledTime();
        String auctionSessionId = notificationLog.getAuctionSessionId();
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).orElse(null);
        LocalDateTime auctionStartTime = auctionSession.getStartTime();

        if (notificationTime.isAfter(LocalDateTime.now()) && notificationLog.getStatus() == NotificationLog.NotificationStatus.SCHEDULED) {
            // Nếu vẫn chưa đến thời gian gửi thông báo, lên lịch lại bình thường
            setSchedulerNotification(notificationLog.getEmail(), auctionSessionId, notificationTime);
        } else if (LocalDateTime.now().isBefore(auctionStartTime) && notificationLog.getStatus() == NotificationLog.NotificationStatus.SCHEDULED) {
            // Nếu đã qua thời gian gửi nhưng chưa đến thời gian bắt đầu phiên đấu giá
            // Gửi thông báo ngay lập tức và cập nhật trạng thái thành SENT
            notificationLog.setStatus(NotificationLog.NotificationStatus.SENT);
            notificationLogRepository.save(notificationLog);

            sendNotification(notificationLog.getEmail(), auctionSession);
        } else {
            // Nếu đã qua cả thời gian bắt đầu phiên đấu giá, đặt trạng thái thành FAILED
            notificationLog.setStatus(NotificationLog.NotificationStatus.FAILED);
            notificationLogRepository.save(notificationLog);
        }
    }

    private void sendNotification(String email, AuctionSession auctionSession) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Auction Session Reminder");
        message.setText("Auction session " + auctionSession.getAsset().getAssetName() + " is about to start");
        javaMailSender.send(message);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        reschedulePendingNotifications();
    }
}
