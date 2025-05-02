package com.example.auction_web.service;

import java.util.List;

import com.example.auction_web.dto.response.FollowResponse;

public interface FollowService {
    FollowResponse followUser(String followerId, String followeeId);
    void unfollowUser(String followerId, String followeeId);
    boolean isFollowing(String followerId, String followeeId);
    List<FollowResponse> getFollowing(String followerId);
    List<FollowResponse> getFollowers(String followeeId);
    long countFollowers(String followeeId);
}
