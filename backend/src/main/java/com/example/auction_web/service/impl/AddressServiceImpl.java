package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AddressMapper;
import com.example.auction_web.repository.AddressRepository;
import com.example.auction_web.repository.DistrictRepository;
import com.example.auction_web.repository.ProvinceRepository;
import com.example.auction_web.repository.WardRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    UserRepository userRepository;
    ProvinceRepository provinceRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;
    AddressMapper addressMapper;

    public AddressResponse createAddress(AddressCreateRequest request) {
        Address address = addressMapper.toAddress(request);

        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var province = provinceRepository.findById(request.getProvinceId()).orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_EXISTED));
        var district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_EXISTED));
        var ward = wardRepository.findById(request.getWardId()).orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));

        address.setUser(user);
        address.setProvince(province);
        address.setDistrict(district);
        address.setWard(ward);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public AddressResponse updateAddress(String id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
        addressMapper.updateAddress(address, request);
        addressRepository.save(address);
        return addressMapper.toAddressResponse(address);
    }

    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    public List<AddressResponse> getAddressByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return addressRepository.findAddressByUser_UserId(userId).stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }
}
