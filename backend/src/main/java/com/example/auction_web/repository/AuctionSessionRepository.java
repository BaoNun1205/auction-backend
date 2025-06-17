package com.example.auction_web.repository;

import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, String>, JpaSpecificationExecutor<AuctionSession> {
    List<AuctionSession> findAuctionSessionByStatusOrderByStartTimeAsc(String status);
    Page<AuctionSession> findAll(Specification<AuctionSession> specification, Pageable pageable);
    List<AuctionSession> findAll(Specification<AuctionSession> specification);
    List<AuctionSession> findByAsset_AssetId(String assetId);

    AuctionSession getAuctionSessionByAsset_AssetId(String assetId);

    @Query("SELECT new com.example.auction_web.dto.response.AuctionSessionInfoDetail(" +
            "asession.auctionSessionId, asession.name, asession.description," +
            "asession.startTime, asession.endTime, asession.startingBids, asession.bidIncrement, asession.status, asession.depositAmount) " +
            "FROM AuctionSession asession " +
            "JOIN asession.asset asset " +
            "WHERE asession.auctionSessionId = :auctionSessionId")
    AuctionSessionInfoDetail findAuctionSessionInfoDetailById(@Param("auctionSessionId") String auctionSessionId);

    // Lấy danh sách người tham gia phiên đấu giá
    @Query("SELECT DISTINCT ah.user FROM AuctionHistory ah " +
           "WHERE ah.auctionSession.auctionSessionId = :auctionSessionId AND ah.delFlag = false")
    List<User> findDistinctUsersByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);

    @Query("SELECT COUNT(a) FROM AuctionSession a WHERE a.delFlag = false")
    long countAllActiveAuctionSessions();

    @Query("SELECT COUNT(a) FROM AuctionSession a WHERE a.delFlag = false AND FUNCTION('YEAR', a.createdAt) = :year")
    long countActiveAuctionSessionsByYear(@Param("year") int year);
}
