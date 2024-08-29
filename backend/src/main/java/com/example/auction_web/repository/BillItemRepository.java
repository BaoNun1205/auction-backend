package com.example.auction_web.repository;

import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, String> {
}
