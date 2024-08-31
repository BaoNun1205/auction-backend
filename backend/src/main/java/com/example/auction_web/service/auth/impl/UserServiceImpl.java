package com.example.auction_web.service.auth.impl;

import com.example.auction_web.constant.PredefinedRole;
import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.auth.Role;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.repository.auth.RoleRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.auth.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Override
    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUer(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }
}
