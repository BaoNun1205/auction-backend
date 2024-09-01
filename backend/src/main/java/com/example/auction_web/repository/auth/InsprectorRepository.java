package com.example.auction_web.repository.auth;

import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsprectorRepository extends JpaRepository<Insprector, String> {
    Insprector findInsprectorByUser(User user);
}
