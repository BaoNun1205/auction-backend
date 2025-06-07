package com.example.auction_web.entity.personalization;

import com.example.auction_web.entity.auth.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String searchId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @Column(nullable = false)
    String keyword;

    int searchCount;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        this.searchCount = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

