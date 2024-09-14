package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.EventCreateRequest;
import com.example.auction_web.dto.request.EventUpdateRequest;
import com.example.auction_web.dto.response.EventResponse;
import com.example.auction_web.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEvent(EventCreateRequest request);
    EventResponse toEventResponse(Event event);
    void updateEvent(@MappingTarget Event event, EventUpdateRequest request);
}
