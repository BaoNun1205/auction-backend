package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.InsprectorMapper;
import com.example.auction_web.repository.auth.InsprectorRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.InsprectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InsprectorServiceImpl implements InsprectorService {
    InsprectorRepository insprectorRepository;
    UserRepository userRepository;
    InsprectorMapper insprectorMapper;

    public InsprectorResponse createInsprector(InsprectorCreateRequest request) {
        var insprector = insprectorMapper.toInsprector(request);

        insprector.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

        return insprectorMapper.toInsprectorResponse(insprectorRepository.save(insprector));
    }

    public InsprectorResponse updateInsprector(String id, InsprectorUpdateRequest request) {
        Insprector insprector = insprectorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INSRECTOR_NOT_EXISTED));
        insprectorMapper.updateInsprector(insprector, request);
        return insprectorMapper.toInsprectorResponse(insprectorRepository.save(insprector));
    }

    public List<InsprectorResponse> getAllInsprectors() {
        return insprectorRepository.findAll().stream()
                .map(insprectorMapper::toInsprectorResponse)
                .toList();
    }

    public InsprectorResponse getInsprectorByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return insprectorMapper.toInsprectorResponse(insprectorRepository.findInsprectorByUser_UserId(userId));
    }
}
