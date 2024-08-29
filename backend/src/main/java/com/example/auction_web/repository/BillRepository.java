package com.example.auction_web.repository;

import com.example.auction_web.entity.Authoritiy;
import com.example.auction_web.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
}
