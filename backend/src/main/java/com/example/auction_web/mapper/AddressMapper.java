package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AddressCreateRequest;
import com.example.auction_web.dto.request.AddressUpdateRequest;
import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.AddressResponse;
import com.example.auction_web.dto.response.CategoryResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressCreateRequest request);
    AddressResponse toAddressResponse(Address address);
    List<AddressResponse> toAddressResponses(List<Address> addresses);
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);
}
