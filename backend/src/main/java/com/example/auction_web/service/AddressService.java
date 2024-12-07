package com.example.auction_web.service;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.entity.auth.User;

import java.util.List;
public interface AddressService {
    AddressResponse createAddress(AddressCreateRequest request);
    AddressResponse updateAddress(String id, AddressUpdateRequest request);
    List<AddressResponse> getAllAddresses();
    List<AddressResponse> getAddressByUserId(String userId);
    AddressResponse getAddressDefaultByUserId(String userId);
}
