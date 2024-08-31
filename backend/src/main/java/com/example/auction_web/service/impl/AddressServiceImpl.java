package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.User;
import com.example.auction_web.mapper.AddressMapper;
import com.example.auction_web.repository.AddressRepository;
import com.example.auction_web.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;

    public AddressResponse createAddress(AddressCreateRequest request) {
        Address address = addressMapper.toAddress(request);
        addressRepository.save(address);
        return addressMapper.toAddressResponse(address);
    }

    public AddressResponse updateAddress(String id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id).orElseThrow();
        addressMapper.updateAddress(address, request);
        addressRepository.save(address);
        return addressMapper.toAddressResponse(address);
    }

    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    public List<AddressResponse> getAddressByUser(User user) {
        List<Address> addresses = addressRepository.findByUser(user).orElseThrow();
        return addressMapper.toAddressResponses(addresses);

    }
}
