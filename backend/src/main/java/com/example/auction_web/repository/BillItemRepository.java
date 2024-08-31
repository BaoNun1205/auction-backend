package com.example.auction_web.repository;

import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, String> {
    List<BillItem> findByBill(Bill bill);
}
