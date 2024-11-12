package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.InspectorCreateRequest;
import com.example.auction_web.dto.request.InspectorUpdateRequest;
import com.example.auction_web.dto.response.InspectorResponse;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InspectorMapper {
    @Mapping(target = "user", ignore = true)
    Inspector toInspector(InspectorCreateRequest request);
    List<Inspector> toInspectors(List<InspectorCreateRequest> requests);

    InspectorResponse toInspectorResponse(Inspector inspector);
    List<InspectorResponse> toInspectorResponses(List<Inspector> inspectors);
    void updateInspector(@MappingTarget Inspector inspector, InspectorUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
