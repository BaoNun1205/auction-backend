package com.example.auction_web.controller;

import com.example.auction_web.dto.request.EventCreateRequest;
import com.example.auction_web.dto.request.EventUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.EventResponse;
import com.example.auction_web.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class EventController {
    EventService eventService;

    @PostMapping
    ApiResponse<EventResponse> createEvent(@RequestBody EventCreateRequest request) {
        return ApiResponse.<EventResponse>builder()
                .code(HttpStatus.OK.value())
                .result(eventService.createEvent(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<EventResponse> updateEvent(@PathVariable String id, @RequestBody EventUpdateRequest request) {
        return ApiResponse.<EventResponse>builder()
                .code(HttpStatus.OK.value())
                .result(eventService.updateEvent(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<EventResponse>> getEvents() {
        return ApiResponse.<List<EventResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(eventService.getAllAEvents())
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<EventResponse> getEventById(@PathVariable String id) {
        return ApiResponse.<EventResponse>builder()
                .code(HttpStatus.OK.value())
                .result(eventService.getEventById(id))
                .build();
    }
}
