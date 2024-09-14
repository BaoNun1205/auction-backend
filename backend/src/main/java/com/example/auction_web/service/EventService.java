package com.example.auction_web.service;

import com.example.auction_web.dto.request.EventCreateRequest;
import com.example.auction_web.dto.request.EventUpdateRequest;
import com.example.auction_web.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse createEvent(EventCreateRequest request);
    EventResponse updateEvent(String id, EventUpdateRequest request);
    List<EventResponse> getAllAEvents();
    EventResponse getEventById(String id);
}
