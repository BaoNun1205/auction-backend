package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "ward")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String wardId;

    @ManyToOne
    @JoinColumn(name = "districtId", referencedColumnName = "districtId")
    District district;

    String wardName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "ward", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AddressDetail addressDetail;
}
