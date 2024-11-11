package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.InspectorCreateRequest;
import com.example.auction_web.dto.request.InspectorUpdateRequest;
import com.example.auction_web.dto.response.InspectorResponse;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.InspectorMapper;
import com.example.auction_web.repository.InspectorRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.InspectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InspectorServiceImpl implements InspectorService {
    InspectorRepository inspectorRepository;
    UserRepository userRepository;
    InspectorMapper inspectorMapper;

    public InspectorResponse createInspector(InspectorCreateRequest request) {
        var insprector = inspectorMapper.toInspector(request);

        insprector.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

        return inspectorMapper.toInspectorResponse(inspectorRepository.save(insprector));
    }

    public InspectorResponse updateInspector(String id, InspectorUpdateRequest request) {
        Inspector insprector = inspectorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INSRECTOR_NOT_EXISTED));
        inspectorMapper.updateInspector(insprector, request);
        return inspectorMapper.toInspectorResponse(inspectorRepository.save(insprector));
    }

    public Inspector getInspectorById(String id) {
        return inspectorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public List<InspectorResponse> getAllInspectors() {
        return inspectorRepository.findAll().stream()
                .map(inspectorMapper::toInspectorResponse)
                .toList();
    }

    public InspectorResponse getInspectorByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return inspectorMapper.toInspectorResponse(inspectorRepository.findInspectorByUser_UserId(userId));
    }
}
