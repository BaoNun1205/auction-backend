package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AddressController {
    AddressService addressService;

    @PostMapping
    ApiResponse<AddressResponse> createAddress(@RequestBody AddressCreateRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.createAddress(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AddressResponse> updateAddress(@PathVariable String id, @RequestBody AddressUpdateRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.updateAddress(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AddressResponse>> getAddresses() {
        return ApiResponse.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.getAllAddresses())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<AddressResponse>> getAddressByUserId(@PathVariable String userId) {
        return ApiResponse.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.getAddressByUserId(userId))
                .build();
    }

    @GetMapping("/default/user/{userId}")
    ApiResponse<AddressResponse> getAddressDefaultByUserId(@PathVariable String userId) {
        return ApiResponse.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .result(addressService.getAddressDefaultByUserId(userId))
                .build();
    }
}
