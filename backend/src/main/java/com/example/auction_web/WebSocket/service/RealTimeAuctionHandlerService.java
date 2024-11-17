package com.example.auction_web.WebSocket.service;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.DepositAddResponse;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;

import java.util.List;

public interface RealTimeAuctionHandlerService {
    SessionJoinResponse joinSession(SessionJoinRequest request, String auctionSessionId);

    DepositAddResponse addDeposit(String auctionSessionId, DepositCreateRequest request);

}
