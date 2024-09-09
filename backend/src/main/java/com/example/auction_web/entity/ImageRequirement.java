package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ImageRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String imageRequirementId;

    @ManyToOne
    @JoinColumn(name = "requirementId", referencedColumnName = "requirementId")
    Requirement requirement;

   String image;
   Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
