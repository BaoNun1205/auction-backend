package com.example.auction_web.service;

import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.RegisterSessionResponse;

import java.util.List;

public interface RegisterSessionService {
    RegisterSessionResponse createRegisterSession(RegisterSessionCreateRequest request);
    RegisterSessionResponse updateRegisterSession(String registerAuctionId, RegisterSessionCreateRequest request);
    List<RegisterSessionResponse> getRegisterSessions();
    RegisterSessionResponse getRegisterSessionByUserAndAuctionSession(String userId, String auctionSessionId);
}
