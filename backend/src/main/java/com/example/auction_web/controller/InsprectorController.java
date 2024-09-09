package com.example.auction_web.controller;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.service.InsprectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insprector")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InsprectorController {
    InsprectorService insprectorService;

    @PostMapping
    ApiResponse<InsprectorResponse> createInsprector(@RequestBody InsprectorCreateRequest request) {
        return ApiResponse.<InsprectorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(insprectorService.createInsprector(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<InsprectorResponse> updateInsprector(@PathVariable String id, @RequestBody InsprectorUpdateRequest request) {
        return ApiResponse.<InsprectorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(insprectorService.updateInsprector(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<InsprectorResponse>> getInsprectors() {
        return ApiResponse.<List<InsprectorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(insprectorService.getAllInsprectors())
                .build();
    }
}
