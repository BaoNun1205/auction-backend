package com.example.auction_web.service;

import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.SessionWinnerResponse;

public interface SessionWinnerService {
    SessionWinnerResponse createSessionWinner(SessionWinnerCreateRequest request);
    SessionWinnerResponse getSessionWinner(String userId);
}
