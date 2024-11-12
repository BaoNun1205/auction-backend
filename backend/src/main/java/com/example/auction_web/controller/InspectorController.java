package com.example.auction_web.controller;

import com.example.auction_web.dto.request.InspectorCreateRequest;
import com.example.auction_web.dto.request.InspectorUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.InspectorResponse;
import com.example.auction_web.service.InspectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspector")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InspectorController {
    InspectorService inspectorService;

    @PostMapping
    ApiResponse<InspectorResponse> createInspector(@RequestBody InspectorCreateRequest request) {
        return ApiResponse.<InspectorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(inspectorService.createInspector(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<InspectorResponse> updateInspector(@PathVariable String id, @RequestBody InspectorUpdateRequest request) {
        return ApiResponse.<InspectorResponse>builder()
                .code(HttpStatus.OK.value())
                .result(inspectorService.updateInspector(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<InspectorResponse>> getInspectors() {
        return ApiResponse.<List<InspectorResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(inspectorService.getAllInspectors())
                .build();
    }
}
