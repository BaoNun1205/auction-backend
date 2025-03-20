package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AutoBidCreateRequest;
import com.example.auction_web.dto.request.AutoBidUpdateRequest;
import com.example.auction_web.dto.response.AutoBidResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.AutoBid;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.AUTOBID;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AutoBidMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.AutoBidRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AutoBidService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AutoBidServiceImpl implements AutoBidService {
    AutoBidRepository autoBidRepository;
    AutoBidMapper autoBidMapper;
    UserRepository userRepository;
    AuctionSessionRepository auctionSessionRepository;

    @Override
    public AutoBidResponse createAutoBid(AutoBidCreateRequest request) {
        try {
            var autoBid = autoBidMapper.toAutoBid(request);
            setReference(request, autoBid);
            return autoBidMapper.toAutoBidResponse(autoBidRepository.save(autoBid));
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_AUTO_BID_FAILED);
        }
    }

    @Override
    public Boolean checkAutoBid(String auctionSessionId, String userId) {
        try {
            return autoBidRepository.existsAutoBidByAuctionSession_AuctionSessionIdAndUser_UserIdAndStatus(auctionSessionId, userId, AUTOBID.ACTIVE);
        } catch (Exception e) {
            throw new AppException(ErrorCode.AUTOBID_NOT_EXISTED);
        }
    }

    @Override
    public AutoBidResponse getAutoBidByAuctionSessionIdAndUserId(String auctionSessionId, String userId) {
        try {
            return autoBidMapper.toAutoBidResponse(autoBidRepository.findAutoBidByAuctionSession_AuctionSessionIdAndUser_UserIdAndStatus(auctionSessionId, userId, AUTOBID.ACTIVE));
        } catch (Exception e) {
            throw new AppException(ErrorCode.AUTOBID_NOT_EXISTED);
        }
    }

    @Override
    public AutoBidResponse updateAutoBid(String id, AutoBidUpdateRequest request) {
        try {
            var autoBid = autoBidRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.AUTOBID_NOT_EXISTED));
            autoBidMapper.updateAutoBid(autoBid, request);
            return autoBidMapper.toAutoBidResponse(autoBidRepository.save(autoBid));
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPDATE_AUTO_BID_FAILED);
        }
    }


    public void setReference(AutoBidCreateRequest request, AutoBid autoBid) {
        autoBid.setUser(getUserById(request.getUserId()));
        autoBid.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public AuctionSession getAuctionSessionById(String auctionSessionId) {
        return auctionSessionRepository.findById(auctionSessionId).orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }
}
