package com.example.auction_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@RequestBody NotificationRequest notificationRequest) {

        Notification notification = Notification.builder()
                    .sender(userService.getUser(notificationRequest.getSenderId()))
                    .receiver(userService.getUser(notificationRequest.getReceiverId()))
                    .type(notificationRequest.getType())
                    .title(notificationRequest.getTitle())
                    .content(notificationRequest.getContent())
                    .referenceId(notificationRequest.getReferenceId())
                    .isRead(false)
                    .build();

        NotificationResponse createdNotification = notificationService.createNotification(notification);
        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(createdNotification)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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
