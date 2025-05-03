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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String reviewId;

    @ManyToOne
    @JoinColumn(name = "reviewerId", referencedColumnName = "userId")
    User reviewer;

    @ManyToOne
    @JoinColumn(name = "revieweeId", referencedColumnName = "userId")
    User reviewee;

    int rating;
    String comment;

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
