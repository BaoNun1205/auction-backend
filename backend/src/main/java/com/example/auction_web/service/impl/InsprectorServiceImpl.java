package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.mapper.InsprectorMapper;
import com.example.auction_web.repository.auth.InsprectorRepository;
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
    InsprectorMapper insprectorMapper;

    public InsprectorResponse createInsprector(InsprectorCreateRequest request) {
        return insprectorMapper.toInsprectorResponse(insprectorRepository.save(insprectorMapper.toInsprector(request)));
    }

    public InsprectorResponse updateInsprector(String id, InsprectorUpdateRequest request) {
        Insprector insprector = insprectorRepository.findById(id).orElseThrow();
        insprectorMapper.updateInsprector(insprector, request);
        return insprectorMapper.toInsprectorResponse(insprectorRepository.save(insprector));
    }

    public List<InsprectorResponse> getAllInsprectors() {
        return insprectorRepository.findAll().stream()
                .map(insprectorMapper::toInsprectorResponse)
                .toList();
    }

    public InsprectorResponse getInsprectorByUser(User user) {
        return insprectorMapper.toInsprectorResponse(insprectorRepository.findInsprectorByUser(user));
    }
}
