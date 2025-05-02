package com.example.auction_web.entity;

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
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String followId;

    @ManyToOne
    @JoinColumn(name = "followerId", referencedColumnName = "userId")
    User follower;

    @ManyToOne
    @JoinColumn(name = "followeeId", referencedColumnName = "userId")
    User followee;

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
