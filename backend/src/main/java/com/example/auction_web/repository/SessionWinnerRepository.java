package com.example.auction_web.repository;

import com.example.auction_web.entity.SessionWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionWinnerRepository extends JpaRepository<SessionWinner, String> {
    SessionWinner getSessionWinnerByUser_UserId(String userId);
    List<SessionWinner> getSessionWinnersByUser_UserId(String userId);
}
