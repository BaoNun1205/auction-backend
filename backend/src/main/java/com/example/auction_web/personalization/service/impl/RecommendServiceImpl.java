package com.example.auction_web.personalization.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.dto.response.UsersJoinSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.personalization.dto.AuctionSessionVector;
import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;
import com.example.auction_web.personalization.service.EmbeddingService;
import com.example.auction_web.personalization.service.SearchHistoryService;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.DepositService;
import com.example.auction_web.service.RegisterSessionService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecommendServiceImpl{
    AuctionSessionRepository auctionSessionRepository;
    EmbeddingService embeddingService;
    SearchHistoryService searchHistoryService;
    RegisterSessionService registerSessionService;
    DepositService depositService;
    AuctionSessionMapper auctionSessionMapper;
    UserMapper userMapper;
    UserRepository userRepository;
    AuctionHistoryRepository auctionHistoryRepository;

    public String buildUserProfileText(String userId) {
        StringBuilder sb = new StringBuilder();

        // 1. Lịch sử tìm kiếm
        List<SearchHistoryResponse> keywords = searchHistoryService.getRecentKeywords(userId);
        keywords.forEach(k -> sb.append(k.getKeyword()).append(" "));

        // 2. Các phiên đã đăng ký
        List<RegisterSessionResponse> registeredSessions = registerSessionService.getRegisterSessionByUserId(userId);
        for (RegisterSessionResponse session : registeredSessions) {
            sb.append(session.getAuctionSession().getName()).append(" ");
            if (session.getAuctionSession().getAsset() != null) {
                sb.append(session.getAuctionSession().getAsset().getAssetName()).append(" ");
                sb.append(session.getAuctionSession().getAsset().getType().getCategoryName()).append(" ");
            }
        }

        // 3. Các phiên đã tham gia
        List<UsersJoinSessionResponse> joinedSessions = depositService.getSessionsJoinByUserId(userId);
        for (UsersJoinSessionResponse session : joinedSessions) {
            sb.append(session.getAuctionSession().getName()).append(" ");
            if (session.getAuctionSession().getAsset() != null) {
                sb.append(session.getAuctionSession().getAsset().getAssetName()).append(" ");
                sb.append(session.getAuctionSession().getAsset().getType().getCategoryName()).append(" ");
            }
        }

        return sb.toString().trim();
    }

    public List<AuctionSessionVector> getAllAuctionSessionVectors() {
        return auctionSessionRepository.findAll().stream()
            .map(session -> {
                String text = session.getName();
                if (session.getAsset() != null) {
                    text += " " + session.getAsset().getAssetName() + " " + session.getAsset().getType().getCategory();
                }
                List<Float> vector = embeddingService.getEmbeddingFromText(text);
                return new AuctionSessionVector(session, vector);
            }).toList();
    }    

    public double cosineSimilarity(List<Float> vec1, List<Float> vec2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
    
        for (int i = 0; i < vec1.size(); i++) {
            dotProduct += vec1.get(i) * vec2.get(i);
            normA += Math.pow(vec1.get(i), 2);
            normB += Math.pow(vec2.get(i), 2);
        }
    
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    
    public List<AuctionSession> recommendSessions(String userId) {
        String profileText = buildUserProfileText(userId);
        List<Float> userVector = embeddingService.getEmbeddingFromText(profileText);

        List<AuctionSessionVector> allSessions = getAllAuctionSessionVectors();

        return allSessions.stream()
            .map(sv -> new AbstractMap.SimpleEntry<>(
                    sv.session(), cosineSimilarity(userVector, sv.vector())))
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(10)
            .map(Map.Entry::getKey)
            .toList();
    }

    public List<AuctionSessionResponse> recommendAuctionSessionResponses(String userId) {
        List<AuctionSession> recommendedSessions = recommendSessions(userId);

        return recommendedSessions.stream()
            .map(auctionSession -> {
                AuctionSessionResponse response = auctionSessionMapper.toAuctionItemResponse(auctionSession);

                List<AuctionSessionInfoResponse> auctionSessionInfoResponse = 
                    auctionHistoryRepository.findAuctionSessionInfo(auctionSession.getAuctionSessionId());

                if (!auctionSessionInfoResponse.isEmpty()) {
                    AuctionSessionInfoResponse info = auctionSessionInfoResponse.get(0);

                    if (info.getHighestBid().compareTo(BigDecimal.ZERO) == 0) {
                        info.setHighestBid(auctionSession.getStartingBids());
                    }

                    if (info.getUserId() != null) {
                        info.setUser(userMapper.toUserResponse(
                            userRepository.findById(info.getUserId()).orElse(null)
                        ));
                    } else {
                        info.setUser(null);
                    }

                    response.setAuctionSessionInfo(info);
                } else {
                    response.setAuctionSessionInfo(
                        new AuctionSessionInfoResponse(0L, 0L, "", auctionSession.getStartingBids(), null)
                    );
                }

                return response;
            })
            .toList();
    }
}
