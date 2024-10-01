package com.example.auction_web.dto.request.auth;

import lombok.Data;

@Data
public class CategoryFilterRequest {
    private Boolean status;
    private String keyword;
}
