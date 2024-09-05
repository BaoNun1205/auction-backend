package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.entity.CoinUser;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoinUserMapper {

    @Mapping(target = "user", ignore = true)
    CoinUser toCoinUser(CoinUserCreateRequest request);
    List<CoinUser> toCoinUserList(List<CoinUserCreateRequest> request);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToString")
    CoinUserResponse toCoinUserResponse(CoinUser coinUser);


    List<CoinUserResponse> toCoinUserResponseList(List<CoinUser> coinUsers);
    void updateCoinUser(@MappingTarget CoinUser coinUser, CoinUserUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
       return user != null ? user.getUserId() : null;
    }
}
