package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.UserCreateRequest;
import com.example.auction_web.dto.response.UserResponse;
import com.example.auction_web.entity.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.repository.UserRepository;
import com.example.auction_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Override
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUer(request);

        return userRepository.save(user);
    }
}
