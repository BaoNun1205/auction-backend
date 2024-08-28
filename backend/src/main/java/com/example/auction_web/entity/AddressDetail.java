package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "address_detail")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String addressDetailId;

    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    Address address;

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


    @OneToOne(mappedBy = "addressDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Bill bill;
}
