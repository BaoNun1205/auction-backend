package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.mapper.BillMapper;
import com.example.auction_web.repository.BillRepository;
import com.example.auction_web.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BillServiceImpl implements BillService {
    BillRepository billRepository;
    BillMapper billMapper;

    public BillResponse createBill(BillCreateRequest request) {
        return billMapper.toBillResponse(billRepository.save(billMapper.toBill(request)));
    }

    public BillResponse updateBill(String id, BillUpdateRequest request) {
        Bill bill = billRepository.findById(id).orElseThrow();
        billMapper.updateBill(bill, request);
        return billMapper.toBillResponse(billRepository.save(bill));
    }

    public List<BillResponse> getAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    public List<BillResponse> getBillsByUserId(String userId) {
        return billRepository.findByUserId(userId).stream()
                .map(billMapper::toBillResponse)
                .toList();
    }
}
