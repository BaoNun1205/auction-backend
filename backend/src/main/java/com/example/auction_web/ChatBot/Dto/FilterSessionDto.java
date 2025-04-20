package com.example.auction_web.ChatBot.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterSessionDto {
    String status;
    String typeId;
    String userId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime toDate;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    String keyword;
    Boolean isInCrease;
    Integer page = 0;
    Integer size = 1000;
}
