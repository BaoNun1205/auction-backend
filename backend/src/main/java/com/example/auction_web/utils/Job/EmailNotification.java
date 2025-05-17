package com.example.auction_web.utils.Job;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.NotificationRepository;
import com.example.auction_web.service.auth.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class EmailNotification implements Job {
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationLogRepository;

    AuctionSessionRepository auctionSessionRepository;
    NotificationStompService notificationService;
    UserService userService;

    @NonFinal
    @Value("${email.username}")
    String EMAIL_ADMIN;

    @Override
    public void execute(JobExecutionContext context) {
        String email = context.getJobDetail().getJobDataMap().getString("email");
        String auctionSessionId = context.getJobDetail().getJobDataMap().getString("auctionSessionId");

        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).get();

        NotificationLog notificationLog = notificationLogRepository.findNotificationLogByAuctionSessionIdAndEmail(auctionSessionId, email);
        try {
            // Tạo và gửi email
            sendNotification(email, auctionSession);

            // Tạo và gửi thông báo socket
            NotificationRequest socketNotification = new NotificationRequest();
            socketNotification.setSenderId(userService.getUserByEmail(EMAIL_ADMIN).getUserId());
            socketNotification.setReceiverId(email);
            socketNotification.setType(NotificationType.AUCTION_REMINDER);
            socketNotification.setTitle("Sắp bắt đầu phiên đấu giá");
            socketNotification.setContent("Phiên đấu giá " + auctionSession.getAsset().getAssetName() + " sắp bắt đầu.");
            socketNotification.setReferenceId(auctionSessionId);

            notificationService.sendUserNotification(email, socketNotification);

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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Nhắc nhở phiên đấu giá");
        message.setText("Phiên đấu giá '" + auctionSession.getAsset().getAssetName() + "' sắp bắt đầu. Vui lòng truy cập hệ thống để tham gia.");
        javaMailSender.send(message);
    }    
}
