package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BalanceUserCreateRequest;
import com.example.auction_web.dto.request.BalanceUserUpdateRequest;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceUserMapper {

    @Mapping(target = "user", ignore = true)
    BalanceUser toBalanceUser(BalanceUserCreateRequest request);
    List<BalanceUser> toBalanceUsers(List<BalanceUserCreateRequest> request);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToString")
    BalanceUserResponse toBalanceUserResponse(BalanceUser balanceUser);


    List<BalanceUserResponse> toBalanceUserResponses(List<BalanceUser> balanceUsers);
    void updateBalance(@MappingTarget BalanceUser balanceUser, BalanceUserUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
       return user != null ? user.getUserId() : null;
    }
}
