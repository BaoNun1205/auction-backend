package com.example.auction_web.repository;

import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
}
