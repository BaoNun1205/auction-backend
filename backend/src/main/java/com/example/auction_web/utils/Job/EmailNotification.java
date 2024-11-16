package com.example.auction_web.utils.Job;

import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailNotification implements Job {
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationLogRepository;

    @Autowired
    private AuctionSessionRepository auctionSessionRepository;

    @Override
    public void execute(JobExecutionContext context) {
        String email = context.getJobDetail().getJobDataMap().getString("email");
        String auctionSessionId = context.getJobDetail().getJobDataMap().getString("auctionSessionId");

        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).get();

        NotificationLog notificationLog = notificationLogRepository.findNotificationLogByAuctionSessionIdAndEmail(auctionSessionId, email);
        try {
            // Tạo và gửi email
            sendNotification(email, auctionSession);

            // Cập nhật trạng thái khi gửi thành công
            if (notificationLog != null) {
                notificationLog.setStatus(NotificationLog.NotificationStatus.SENT);
                notificationLog.setSentTime(LocalDateTime.now());
                notificationLogRepository.save(notificationLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Cập nhật trạng thái khi gửi thất bại
            if (notificationLog != null) {
                notificationLog.setStatus(NotificationLog.NotificationStatus.FAILED);
                notificationLogRepository.save(notificationLog);
            }
        }
    }

    private void sendNotification(String email, AuctionSession auctionSession) {
        // Thực hiện gửi thông báo ngay lập tức (ví dụ qua Email)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Auction Session Reminder");
        message.setText("Auction session " + auctionSession.getAsset().getAssetName() + " is about to start");
        javaMailSender.send(message);
    }
}
