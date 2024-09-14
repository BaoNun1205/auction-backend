package com.example.auction_web.repository;

import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    Bill findBillByDeposit_DepositId(String depositId);
}
