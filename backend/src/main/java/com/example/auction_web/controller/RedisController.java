package com.example.auction_web.controller;

import com.example.auction_web.dto.request.KeyValueRequest;
import com.example.auction_web.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RedisController {
    RedisService redisService;

    @PostMapping
    void set(@RequestBody KeyValueRequest keyValueRequest) {
        redisService.set(keyValueRequest.getKey(), keyValueRequest.getValue());
    }
}
