package com.example.auction_web.ChatBot.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.example.auction_web.ChatBot.Enum.Role;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String conversationId;

    @Enumerated(EnumType.STRING)
    Role role;

    @Lob
    @Column(columnDefinition = "TEXT")
    String content;

    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
