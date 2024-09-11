package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.District;
import com.example.auction_web.entity.Province;
import com.example.auction_web.entity.Ward;
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
import jakarta.persistence.Cacheable;
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

        setAddressReference(request, address);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public AddressResponse updateAddress(String id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
        addressMapper.updateAddress(address, request);

        setAddressReference(request, address);

        return addressMapper.toAddressResponse(addressRepository.save(address));
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

    void setAddressReference(Object request, Address address) {
        if (request instanceof AddressCreateRequest createRequest) {
            address.setUser(getUserById(createRequest.getUserId()));
            address.setProvince(getProvinceById(createRequest.getProvinceId()));
            address.setDistrict(getDistrictById(createRequest.getDistrictId()));
            address.setWard(getWardById(createRequest.getWardId()));
        } else if (request instanceof AddressUpdateRequest updateRequest) {
            address.setProvince(getProvinceById(updateRequest.getProvinceId()));
            address.setDistrict(getDistrictById(updateRequest.getDistrictId()));
            address.setWard(getWardById(updateRequest.getWardId()));
        }
    }

    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    Province getProvinceById(String provinceId) {
        return provinceRepository.findById(provinceId)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_EXISTED));
    }

    District getDistrictById(String districtId) {
        return districtRepository.findById(districtId)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_EXISTED));
    }

    Ward getWardById(String wardId) {
        return wardRepository.findById(wardId)
                .orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
    }
}
