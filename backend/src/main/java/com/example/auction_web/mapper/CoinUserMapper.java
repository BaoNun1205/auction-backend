package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.entity.CoinUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoinUserMapper {
    CoinUser toCoinUser(CoinUserCreateRequest request);
    List<CoinUser> toCoinUserList(List<CoinUserCreateRequest> request);
    CoinUserResponse toCoinUserResponse(CoinUser coinUser);
    List<CoinUserResponse> toCoinUserResponseList(List<CoinUser> coinUsers);
    void updateCoinUser(@MappingTarget CoinUser coinUser, CoinUserUpdateRequest request);
}
