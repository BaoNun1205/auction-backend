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
    AddressMapper addressMapper;


    public AddressResponse createAddress(AddressCreateRequest request) {
        Address address = addressMapper.toAddress(request);

        if (request.getIsDefault() != null && request.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findAddressByUser_UserId(request.getUserId()); // Giả sử bạn có một phương thức để lấy tất cả địa chỉ của người dùng
            for (Address existingAddress : existingAddresses) {
                if (existingAddress.getIsDefault()) {
                    existingAddress.setIsDefault(false);
                    addressRepository.save(existingAddress);
                }
            }
        }

        setAddressReference(request, address);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public AddressResponse updateAddress(String id, AddressUpdateRequest request) {
        // Tìm địa chỉ cần cập nhật
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        // Nếu yêu cầu cập nhật có isDefault = true, ta thực hiện đặt địa chỉ mặc định
        if (request.getIsDefault() != null && request.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findAddressByUser_UserId(address.getUser().getUserId());

            // Đặt tất cả các địa chỉ khác thành không mặc định
            for (Address existingAddress : existingAddresses) {
                if (existingAddress.getIsDefault()) {
                    existingAddress.setIsDefault(false);
                    addressRepository.save(existingAddress);
                }
            }

            // Cập nhật địa chỉ hiện tại thành mặc định
            address.setIsDefault(true);
        } else {
            // Nếu isDefault là false hoặc không có yêu cầu cập nhật, chỉ cập nhật các trường còn lại
            address.setIsDefault(false);
        }

        // Cập nhật các trường còn lại của địa chỉ từ request
        addressMapper.updateAddress(address, request);

        // Lưu lại địa chỉ đã được cập nhật
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

    public AddressResponse getAddressDefaultByUserId(String userId) {
        return addressMapper.toAddressResponse(addressRepository.findAddressByUser_UserIdAndIsDefaultTrue(userId));
    }

    void setAddressReference(AddressCreateRequest request, Address address) {
        address.setUser(getUserById(request.getUserId()));
    }

    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

}
