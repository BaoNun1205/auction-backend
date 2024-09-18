package com.example.auction_web.WebSocket.service;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.service.RegisterSessionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RealTimeAuctionHandlerServiceImpl implements RealTimeAuctionHandlerService{
    RegisterSessionService registerSessionService;

    @Override
    public SessionJoinResponse joinSession(SessionJoinRequest request, String auctionSessionId) {
        if (registerSessionService.getRegisterSessionByUserAndAuctionSession(request.getUserId(), auctionSessionId) != null) {
            throw new AppException(ErrorCode.REGISTER_SESSION_EXISTED);
        }
        RegisterSessionCreateRequest registerSessionCreateRequest = new RegisterSessionCreateRequest(
                request.getUserId(),
                auctionSessionId,
                false,
                request.getCreatedAt(),
                request.getCreatedAt()
        );
        registerSessionService.createRegisterSession(registerSessionCreateRequest);
        return new SessionJoinResponse(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getCreatedAt()
        );
    }
}
