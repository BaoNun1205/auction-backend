package com.example.auction_web.WebSocket.service;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.DepositAddResponse;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.DepositService;
import com.example.auction_web.service.RegisterSessionService;
import com.example.auction_web.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RealTimeAuctionHandlerServiceImpl implements RealTimeAuctionHandlerService{
    RegisterSessionService registerSessionService;
    DepositService depositService;
    UserService userService;
    AuctionSessionService auctionSessionService;

    @Override
    public SessionJoinResponse joinSession(SessionJoinRequest request, String auctionSessionId) {
        if (registerSessionService.getRegisterSessionByUserAndAuctionSession(request.getUserId(), auctionSessionId) != null) {
            throw new AppException(ErrorCode.REGISTER_SESSION_EXISTED);
        }
        RegisterSessionCreateRequest registerSessionCreateRequest = new RegisterSessionCreateRequest(
                request.getUserId(),
                auctionSessionId
        );
        registerSessionService.createRegisterSession(registerSessionCreateRequest);
        return new SessionJoinResponse(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getCreatedAt()
        );
    }

    @Override
    public DepositAddResponse addDeposit(String auctionSessionId, DepositCreateRequest request) {
        BigDecimal maxDeposit = depositService.maxDepositPriceByAuctionSessionId(auctionSessionId);
        BigDecimal depositPrice = new BigDecimal(String.valueOf(request.getDepositPrice()));
        if (depositPrice.compareTo(maxDeposit) > 0) {
            throw new AppException(ErrorCode.DEPOSIT_PRICE_IS_GREATER_THAN_MAX_DEPOSIT);
        }
        UserResponse userResponse = userService.getUserResponse(request.getUserId());
        AuctionSessionResponse auctionSessionResponse = auctionSessionService.getAuctionSessionById(auctionSessionId);
        depositService.createDeposit(request);
        return DepositAddResponse.builder()
                .userId(request.getUserId())
                .name(userResponse.getName())
                .auctionSessionId(auctionSessionId)
                .typeAuctionSession(auctionSessionResponse.getTypeSession())
                .depositPrice(String.valueOf(request.getDepositPrice()))
                .build();
    }


}
