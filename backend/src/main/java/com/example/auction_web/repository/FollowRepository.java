package com.example.auction_web.repository;

import com.example.auction_web.entity.Follow;
import com.example.auction_web.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, String> {
    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
    Optional<Follow> findByFollowerAndFolloweeAndDelFlagFalse(User follower, User followee);
    List<Follow> findByFollowerAndDelFlagFalse(User follower);
    List<Follow> findByFolloweeAndDelFlagFalse(User followee);
    long countByFolloweeAndDelFlagFalse(User followee);
}
