package com.example.auction_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.service.notification.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Lấy danh sách thông báo của người dùng
    @GetMapping("/user/{receiverId}")
    public ApiResponse<List<NotificationResponse>> getNotifications(@PathVariable String receiverId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(notificationService.getNotificationByReceiver(receiverId))
                .build();
    }

    // Đếm số thông báo chưa đọc
    @GetMapping("/unread/count/{receiverId}")
    public ApiResponse<Long> countUnreadNotifications(@PathVariable String receiverId) {
        return ApiResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .result(notificationService.countUnreadNotifications(receiverId))
                .build();
    }

    // Đánh dấu thông báo là đã đọc
    @PutMapping("/read/{notificationId}")
    public ApiResponse<String> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Notification marked as read")
                .build();
    }
}
