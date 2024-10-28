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
    Address toAddress(AddressCreateRequest request);
    AddressResponse toAddressResponse(Address address);
    List<AddressResponse> toAddressResponses(List<Address> addresses);
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);
}
