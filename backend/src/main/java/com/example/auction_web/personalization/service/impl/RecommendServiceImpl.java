package com.example.auction_web.personalization.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.dto.response.UsersJoinSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;
import com.example.auction_web.personalization.service.EmbeddingService;
import com.example.auction_web.personalization.service.RecommendService;
import com.example.auction_web.personalization.service.SearchHistoryService;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.RegisterSessionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.DepositService;
import com.example.auction_web.service.RegisterSessionService;
import com.example.auction_web.utils.VectorUtil;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecommendServiceImpl implements RecommendService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendServiceImpl.class);

    AuctionSessionRepository auctionSessionRepository;
    EmbeddingService embeddingService;
    SearchHistoryService searchHistoryService;
    RegisterSessionService registerSessionService;
    DepositService depositService;
    AuctionSessionMapper auctionSessionMapper;
    UserMapper userMapper;
    UserRepository userRepository;
    AuctionHistoryRepository auctionHistoryRepository;
    RegisterSessionRepository registerSessionRepository;

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
                if (session.getAuctionSession().getAsset().getType() != null 
                        && session.getAuctionSession().getAsset().getType().getCategoryId() != null) {
                    sb.append(session.getAuctionSession().getAsset().getType().getCategoryName()).append(" ");
                }
            }
        }

        // 3. Các phiên đã tham gia
        List<UsersJoinSessionResponse> joinedSessions = depositService.getSessionsJoinByUserId(userId);
        for (UsersJoinSessionResponse session : joinedSessions) {
            sb.append(session.getAuctionSession().getName()).append(" ");
            if (session.getAuctionSession().getAsset() != null) {
                sb.append(session.getAuctionSession().getAsset().getAssetName()).append(" ");
                if (session.getAuctionSession().getAsset().getType() != null 
                        && session.getAuctionSession().getAsset().getType().getCategoryId() != null) {
                    sb.append(session.getAuctionSession().getAsset().getType().getCategoryName()).append(" ");
                }
            }
        }

        return sb.toString().trim();
    }

    @Transactional
    public void batchUpdateUserVectors() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            try {
                String profileText = buildUserProfileText(user.getUserId());
                if (profileText.isBlank()) {
                    logger.warn("Empty profile text for user ID: {}", user.getUserId());
                    continue;
                }
                List<Float> userVector = embeddingService.getEmbeddingFromText(profileText);
                if (userVector == null || userVector.isEmpty()) {
                    logger.warn("Null or empty vector for user ID: {}", user.getUserId());
                    continue;
                }
                String json = VectorUtil.toJson(userVector);
                user.setVectorJson(json);
            } catch (Exception e) {
                logger.error("Failed to update vector for user ID: {}", user.getUserId(), e);
            }
        }
        userRepository.saveAll(users);
    }

    @Transactional
    public void batchUpdateAuctionSessionVectors() {
        List<AuctionSession> sessions = auctionSessionRepository.findAll();
        for (AuctionSession session : sessions) {
            try {
                String text = session.getName();
                if (session.getAsset() != null) {
                    text += " " + session.getAsset().getAssetName();
                    if (session.getAsset().getType() != null && session.getAsset().getType().getCategory() != null) {
                        text += " " + session.getAsset().getType().getCategory().getCategoryName();
                    }
                }
                if (text.isBlank()) {
                    logger.warn("Empty text for auction session ID: {}", session.getAuctionSessionId());
                    continue;
                }
                List<Float> vector = embeddingService.getEmbeddingFromText(text);
                if (vector == null || vector.isEmpty()) {
                    logger.warn("Null or empty vector for auction session ID: {}", session.getAuctionSessionId());
                    continue;
                }
                String json = VectorUtil.toJson(vector);
                session.setVectorJson(json);
            } catch (Exception e) {
                logger.error("Failed to update vector for auction session ID: {}", session.getAuctionSessionId(), e);
            }
        }
        auctionSessionRepository.saveAll(sessions);
    }

    public double cosineSimilarity(List<Float> vec1, List<Float> vec2) {
        if (vec1 == null || vec2 == null || vec1.isEmpty() || vec2.isEmpty()) {
            logger.warn("Invalid vectors for cosine similarity: vec1={}, vec2={}", vec1, vec2);
            return 0.0;
        }

        int minLength = Math.min(vec1.size(), vec2.size());
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < minLength; i++) {
            dotProduct += vec1.get(i) * vec2.get(i);
            normA += Math.pow(vec1.get(i), 2);
            normB += Math.pow(vec2.get(i), 2);
        }

        if (normA == 0.0 || normB == 0.0) {
            logger.warn("Zero norm in cosine similarity: normA={}, normB={}", normA, normB);
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // public List<AuctionSession> recommendSessions(String userId, String status) {
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    //     String vectorJson = user.getVectorJson();
    //     if (vectorJson == null) {
    //         logger.warn("No vector JSON for user ID: {}", userId);
    //         return List.of();
    //     }
    //     List<Float> userVector = VectorUtil.fromJson(vectorJson);

    //     List<AuctionSession> allSessions = auctionSessionRepository.findAll();

    //     return allSessions.stream()
    //         .filter(session -> session.getVectorJson() != null)
    //         .filter(session -> session.getStatus().equals(status))
    //         .map(session -> new AbstractMap.SimpleEntry<>(
    //                 session,
    //                 cosineSimilarity(userVector, VectorUtil.fromJson(session.getVectorJson()))))
    //         .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
    //         .limit(10)
    //         .map(Map.Entry::getKey)
    //         .toList();
    // }

    public List<AuctionSession> recommendSessions(String userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        String vectorJson = user.getVectorJson();
        if (vectorJson == null) {
            logger.warn("No vector JSON for user ID: {}", userId);
            return List.of();
        }
        List<Float> userVector = VectorUtil.fromJson(vectorJson);

        List<AuctionSession> allSessions = auctionSessionRepository.findAll();

        AUCTION_STATUS auctionStatus;
        try {
            auctionStatus = AUCTION_STATUS.valueOf(status);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid auction status: {}", status);
            return List.of();
        }

        if (auctionStatus == AUCTION_STATUS.HOT) {
            return allSessions.stream()
            .filter(session -> session.getVectorJson() != null)
            .map(session -> {
                List<AuctionSessionInfoResponse> infoList = auctionHistoryRepository
                    .findAuctionSessionInfo(session.getAuctionSessionId());
                long bidCount = infoList.isEmpty() ? 0 : infoList.get(0).getTotalAuctionHistory();
                double similarity = cosineSimilarity(userVector, VectorUtil.fromJson(session.getVectorJson()));
                return new AbstractMap.SimpleEntry<>(session, new double[]{bidCount, similarity});
            })
            .sorted((a, b) -> {
                // Compare by bid count first (descending)
                int bidCompare = Double.compare(b.getValue()[0], a.getValue()[0]);
                if (bidCompare != 0) {
                    return bidCompare;
                }
                // If bid counts are equal, compare by similarity (descending)
                return Double.compare(b.getValue()[1], a.getValue()[1]);
            })
            .limit(10)
            .map(Map.Entry::getKey)
            .toList();
        } else {
            return allSessions.stream()
                .filter(session -> session.getVectorJson() != null)
                .filter(session -> {
                    try {
                        return AUCTION_STATUS.valueOf(session.getStatus()).equals(auctionStatus);
                    } catch (IllegalArgumentException e) {
                        return false; // Skip sessions with invalid status
                    }
                })
                .map(session -> new AbstractMap.SimpleEntry<>(
                        session,
                        cosineSimilarity(userVector, VectorUtil.fromJson(session.getVectorJson()))))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
        }
    }

    public List<AuctionSessionResponse> recommendAuctionSessionResponses(String userId, String status) {
        List<AuctionSession> recommendedSessions = recommendSessions(userId, status);

        // Collect all user IDs needed for mapping
        Set<String> userIds = recommendedSessions.stream()
            .flatMap(auctionSession -> auctionHistoryRepository
                .findAuctionSessionInfo(auctionSession.getAuctionSessionId())
                .stream()
                .filter(info -> info.getUserId() != null)
                .map(AuctionSessionInfoResponse::getUserId))
            .collect(Collectors.toSet());

        // Batch fetch users
        Map<String, User> userMap = userRepository.findAllById(userIds)
            .stream()
            .collect(Collectors.toMap(User::getUserId, user -> user));

        return recommendedSessions.stream()
            .map(auctionSession -> {
                AuctionSessionResponse response = auctionSessionMapper.toAuctionItemResponse(auctionSession);

                List<AuctionSessionInfoResponse> auctionSessionInfoResponse = 
                    auctionHistoryRepository.findAuctionSessionInfo(auctionSession.getAuctionSessionId());

                int totalRegistrations = registerSessionRepository
                    .countRegisterSessionsByAuctionSession_AuctionSessionId(auctionSession.getAuctionSessionId());

                if (!auctionSessionInfoResponse.isEmpty()) {
                    AuctionSessionInfoResponse info = auctionSessionInfoResponse.get(0);

                    if (info.getHighestBid().compareTo(BigDecimal.ZERO) == 0) {
                        info.setHighestBid(auctionSession.getStartingBids());
                    }

                    if (info.getUserId() != null) {
                        User user = userMap.get(info.getUserId());
                        info.setUser(user != null ? userRepository.findUserInfoBaseByUserId(info.getUserId()) : null);
                    } else {
                        info.setUser(null);
                    }
                    info.setTotalRegistrations(totalRegistrations);
                    response.setAuctionSessionInfo(info);
                } else {
                    response.setAuctionSessionInfo(
                        new AuctionSessionInfoResponse(0L, 0L, "", auctionSession.getStartingBids(), null, totalRegistrations)
                    );
                }

                return response;
            })
            .toList();
    }

    public List<AuctionSession> recommendSessionsByAuctionSessionId(String auctionSessionId) {
        AuctionSession baseSession = auctionSessionRepository.findById(auctionSessionId)
            .orElseThrow(() -> new IllegalArgumentException("Auction session not found with ID: " + auctionSessionId));
    
        if (baseSession.getVectorJson() == null) {
            logger.warn("No vector JSON for auction session ID: {}", auctionSessionId);
            return List.of();
        }
    
        List<Float> baseVector = VectorUtil.fromJson(baseSession.getVectorJson());
    
        List<AuctionSession> allSessions = auctionSessionRepository.findAll();
    
        return allSessions.stream()
            .filter(session -> session.getAuctionSessionId() != auctionSessionId)
            .filter(session -> session.getVectorJson() != null)
            .map(session -> new AbstractMap.SimpleEntry<>(
                    session, 
                    cosineSimilarity(baseVector, VectorUtil.fromJson(session.getVectorJson()))))
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(5)
            .map(Map.Entry::getKey)
            .toList();
    }

    public List<AuctionSessionResponse> recommendAuctionSessionResponsesByAuctionSessionId(String auctionSessionId) {
        List<AuctionSession> recommendedSessions = recommendSessionsByAuctionSessionId(auctionSessionId);
    
        Set<String> userIds = recommendedSessions.stream()
            .flatMap(session -> auctionHistoryRepository
                .findAuctionSessionInfo(session.getAuctionSessionId())
                .stream()
                .filter(info -> info.getUserId() != null)
                .map(AuctionSessionInfoResponse::getUserId))
            .collect(Collectors.toSet());
    
        Map<String, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getUserId, user -> user));
    
        return recommendedSessions.stream()
            .map(session -> {
                AuctionSessionResponse response = auctionSessionMapper.toAuctionItemResponse(session);
    
                List<AuctionSessionInfoResponse> auctionSessionInfoResponse = 
                    auctionHistoryRepository.findAuctionSessionInfo(session.getAuctionSessionId());
    
                if (!auctionSessionInfoResponse.isEmpty()) {
                    AuctionSessionInfoResponse info = auctionSessionInfoResponse.get(0);
    
                    if (info.getHighestBid().compareTo(BigDecimal.ZERO) == 0) {
                        info.setHighestBid(session.getStartingBids());
                    }
    
                    if (info.getUserId() != null) {
                        User user = userMap.get(info.getUserId());
                        info.setUser(user != null ? userRepository.findUserInfoBaseByUserId(info.getUserId()) : null);
                    } else {
                        info.setUser(null);
                    }
                    info.setTotalRegistrations(registerSessionRepository.countRegisterSessionsByAuctionSession_AuctionSessionId(session.getAuctionSessionId()));
                    response.setAuctionSessionInfo(info);
                } else {
                    response.setAuctionSessionInfo(
                        new AuctionSessionInfoResponse(0L, 0L, "", session.getStartingBids(), null)
                    );
                }
    
                return response;
            })
            .toList();
    }    
}