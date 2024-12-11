package com.example.auction_web.service;

import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.RegisterSessionResponse;

import java.util.List;

public interface RegisterSessionService {
    RegisterSessionResponse createRegisterSession(RegisterSessionCreateRequest request);
    RegisterSessionResponse updateRegisterSession(String registerAuctionId, RegisterSessionCreateRequest request);
    void unRegisterSession(RegisterSessionCreateRequest request);
    List<RegisterSessionResponse> getRegisterSessions();
    Boolean getRegisterSessionByUserAndAuctionSession(String userId, String auctionSessionId);
    List<RegisterSessionResponse> getRegisterSessionByUserId(String userId);
    List<RegisterSessionResponse> getRegisterSessionByAuctionSessionId(String auctionSessionId);
}
