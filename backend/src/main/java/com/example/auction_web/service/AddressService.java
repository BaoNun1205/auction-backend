package com.example.auction_web.service;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
@FieldDefaults(level = AccessLevel.PUBLIC)
public interface AddressService {
    AddressResponse createAddress(AddressCreateRequest request);
    AddressResponse updateAddress(String id, AddressUpdateRequest request);
    List<AddressResponse> getAllAddresses();
    List<AddressResponse> getAddressByUserId(String userId);
}
