package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "bill")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String billId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    LocalDateTime billDate;

    @OneToOne
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    Address address;

    @Column(precision = 15, scale = 0)
    BigDecimal despositPrice;

    @Column(precision = 15, scale = 0)
    BigDecimal totalProfitPrice;

    @Column(precision = 15, scale = 0)
    BigDecimal totalPrice;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BillItem> billItems;
}
