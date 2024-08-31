package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "address")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String addressId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    String recipientName;

    @OneToOne
    @JoinColumn(name = "provinceId", referencedColumnName = "provinceId")
    Province province;

    @OneToOne
    @JoinColumn(name = "districtId", referencedColumnName = "districtId")
    District district;

    @OneToOne
    @JoinColumn(name = "wardId", referencedColumnName = "wardId")
    Ward ward;

    String addressDetail;
    String phone;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Bill bill;
}
