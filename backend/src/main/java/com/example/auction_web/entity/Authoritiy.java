package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "authority")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Authoritiy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String authorityId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @OneToOne
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    Role role;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
