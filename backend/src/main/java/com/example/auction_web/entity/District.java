package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "district")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String districtId;

    @ManyToOne
    @JoinColumn(name = "provinceId", referencedColumnName = "provinceId")
    Province province;

    String districtName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AddressDetail addressDetail;
}
