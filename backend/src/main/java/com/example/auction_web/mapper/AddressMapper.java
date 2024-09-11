package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.dto.response.CategoryResponse;
import com.example.auction_web.entity.*;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "ward", ignore = true)
    Address toAddress(AddressCreateRequest request);


    @Mapping(source = "user", target = "userId", qualifiedByName = "userToString")
    @Mapping(source = "province", target = "provinceId", qualifiedByName = "provinceToString")
    @Mapping(source = "district", target = "districtId", qualifiedByName = "districtToString")
    @Mapping(source = "ward", target = "wardId", qualifiedByName = "wardToString")
    AddressResponse toAddressResponse(Address address);


    List<AddressResponse> toAddressResponses(List<Address> addresses);

    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "ward", ignore = true)
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("provinceToString")
    default String provinceToString(Province province) {
        return province != null ? province.getProvinceId() : null;
    }

    @Named("districtToString")
    default String districtToString(District district) {
        return district != null ? district.getDistrictId() : null;
    }

    @Named("wardToString")
    default String wardToString(Ward ward) {
        return ward != null ? ward.getWardId() : null;
    }
}
