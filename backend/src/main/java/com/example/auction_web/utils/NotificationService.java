package com.example.auction_web.utils;

import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.service.AuctionSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;
    private final AuctionSessionService auctionSessionService;
    private final TaskScheduler taskScheduler;

    public void scheduleNotification(String email, AuctionSession auctionSession, LocalDateTime nofificationTime) {
        Runnable task = () -> sendNotification(email, auctionSession);
        Date date = Date.from(nofificationTime.atZone(ZoneId.systemDefault()).toInstant());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, date);
    }

    private void sendNotification(String email, AuctionSession auctionSession) {
        // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Auction Session Reminder");
        message.setText("Auction session " + auctionSession.getAuctionSessionId() + " is about to start");
        javaMailSender.send(message);
    }
}
