package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.EventCreateRequest;
import com.example.auction_web.dto.request.EventUpdateRequest;
import com.example.auction_web.dto.response.EventResponse;
import com.example.auction_web.entity.Event;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.EventMapper;
import com.example.auction_web.repository.EventRepository;
import com.example.auction_web.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    EventMapper eventMapper;

    public EventResponse createEvent(EventCreateRequest request) {
        return eventMapper.toEventResponse(eventRepository.save(eventMapper.toEvent(request)));
    }

    public EventResponse updateEvent(String id, EventUpdateRequest request) {
        Event event = eventRepository.findById(id).orElseThrow();
        eventMapper.updateEvent(event, request);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAllAEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toEventResponse)
                .toList();
    }

    public EventResponse getEventById(String id) {
        var auctionSession = eventRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXIST));
        return eventMapper.toEventResponse(auctionSession);
    }
}
