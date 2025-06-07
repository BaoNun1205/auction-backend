package com.example.auction_web.service.impl;

import com.example.auction_web.dto.response.FollowResponse;
import com.example.auction_web.entity.Follow;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.mapper.FollowMapper;
import com.example.auction_web.repository.FollowRepository;
import com.example.auction_web.service.FollowService;
import com.example.auction_web.service.auth.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FollowServiceImpl implements FollowService {

    FollowRepository followRepository;
    UserService userService;
    FollowMapper followMapper;

    public FollowResponse followUser(String followerId, String followeeId) {
        User follower = userService.getUser(followerId);
        User followee = userService.getUser(followeeId);

        Optional<Follow> existing = followRepository.findByFollowerAndFollowee(follower, followee);

        if (existing.isPresent()) {
            Follow follow = existing.get();
            if (follow.getDelFlag()) {
                follow.setDelFlag(false);
                follow.setUpdatedAt(LocalDateTime.now());
                return followMapper.toFollowResponse(followRepository.save(follow));
            }
            return followMapper.toFollowResponse(follow);
        }

        Follow follow = Follow.builder()
            .follower(follower)
            .followee(followee)
            .build();

        return followMapper.toFollowResponse(followRepository.save(follow));
    }

    public void unfollowUser(String followerId, String followeeId) {
        User follower = userService.getUser(followerId);
        User followee = userService.getUser(followeeId);

        followRepository.findByFollowerAndFolloweeAndDelFlagFalse(follower, followee)
            .ifPresent(follow -> {
                follow.setDelFlag(true);
                follow.setUpdatedAt(LocalDateTime.now());
                followRepository.save(follow);
            });
    }

    public boolean isFollowing(String followerId, String followeeId) {
        User follower = userService.getUser(followerId);
        User followee = userService.getUser(followeeId);

        return followRepository.findByFollowerAndFolloweeAndDelFlagFalse(follower, followee).isPresent();
    }

    public List<FollowResponse> getFollowing(String followerId) {
        User follower = userService.getUser(followerId);
        return followRepository.findByFollowerAndDelFlagFalse(follower)
            .stream()
            .map(followMapper::toFollowResponse)
            .toList();
    }

    public List<FollowResponse> getFollowers(String followeeId) {
        User followee = userService.getUser(followeeId);
        return followRepository.findByFolloweeAndDelFlagFalse(followee)
            .stream()
            .map(followMapper::toFollowResponse)
            .toList();
    }

    public long countFollowers(String followeeId) {
        User followee = userService.getUser(followeeId);
        return followRepository.countByFolloweeAndDelFlagFalse(followee);
    }
}
