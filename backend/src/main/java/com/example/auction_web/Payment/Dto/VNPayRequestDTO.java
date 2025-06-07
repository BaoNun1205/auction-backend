package com.example.auction_web.Payment.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRequestDTO {
    private String amount;
    private String bankCode;
    private String userId;

}
