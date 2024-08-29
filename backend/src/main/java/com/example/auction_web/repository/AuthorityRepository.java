package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionHistoryDetail;
import com.example.auction_web.entity.Authoritiy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authoritiy, String> {
}
