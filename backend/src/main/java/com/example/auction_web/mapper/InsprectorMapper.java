package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InsprectorMapper {
    @Mapping(target = "user", ignore = true)
    Insprector toInsprector(InsprectorCreateRequest request);
    List<Insprector> toInsprectors(List<InsprectorCreateRequest> requests);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToString")
    InsprectorResponse toInsprectorResponse(Insprector insprector);
    List<InsprectorResponse> toInsprectorResponses(List<Insprector> insprectors);
    void updateInsprector(@MappingTarget Insprector insprector, InsprectorUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
