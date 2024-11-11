package com.example.auction_web.dto.response;

import com.example.auction_web.entity.ImageAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionInfoDetail {
    String id;
    String assetName;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<ImageAssetResponse> listImage;

    public AuctionSessionInfoDetail(String id, String assetName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.assetName = assetName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}