package com.example.auction_web.WebSocket.service;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;

public interface RealTimeAuctionHandlerService {
    SessionJoinResponse joinSession(SessionJoinRequest request, String auctionSessionId);
}
