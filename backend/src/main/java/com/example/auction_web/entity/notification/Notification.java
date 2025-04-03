package com.example.auction_web.entity.notification;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.NotificationType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "userId")
    User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId")
    User receiver;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    String content;

    @Column(name = "reference_id")
    String referenceId;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    boolean isRead = false;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
