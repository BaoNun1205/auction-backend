package com.example.auction_web.repository;

import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
    List<Bill> findByUser(User user);
}
