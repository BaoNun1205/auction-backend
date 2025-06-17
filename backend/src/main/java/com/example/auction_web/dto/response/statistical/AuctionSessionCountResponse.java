package com.example.auction_web.dto.response.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionSessionCountResponse {
    private long totalCount;
    private long countOfYear;
    private double growthRate;
}
