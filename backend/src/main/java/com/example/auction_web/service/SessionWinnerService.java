package com.example.auction_web.service;

import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.SessionWinnerResponse;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.SESSION_WIN_STATUS;

import java.util.List;

public interface SessionWinnerService {
    SessionWinnerResponse createSessionWinner(SessionWinnerCreateRequest request);
    SessionWinnerResponse getSessionWinner(String userId);
    List<SessionWinnerResponse> getSessionsWinner(String userId);
    SessionWinnerResponse getSessionWinnerByAuctionSessionId(String auctionSessionId);
    SessionWinnerResponse updateSessionWinnerStatus(String sessionWinnerId, SESSION_WIN_STATUS status);
    AssetResponse updateAssetStatus(String assetId, ASSET_STATUS status);
}
