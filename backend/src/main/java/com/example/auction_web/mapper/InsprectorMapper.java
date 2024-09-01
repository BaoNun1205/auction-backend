package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.entity.auth.Insprector;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InsprectorMapper {
    Insprector toInsprector(InsprectorCreateRequest request);
    List<Insprector> toInsprectors(List<InsprectorCreateRequest> requests);
    InsprectorResponse toInsprectorResponse(Insprector insprector);
    List<InsprectorResponse> toInsprectorResponses(List<Insprector> insprectors);
    void updateInsprector(@MappingTarget Insprector insprector, InsprectorUpdateRequest request);
}
