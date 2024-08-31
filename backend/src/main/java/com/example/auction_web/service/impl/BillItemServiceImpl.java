package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.BillItemResponse;
import com.example.auction_web.entity.BillItem;
import com.example.auction_web.mapper.BillItemMapper;
import com.example.auction_web.repository.BillItemRepository;
import com.example.auction_web.service.BillItemService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BillItemServiceImpl implements BillItemService {
    BillItemRepository billItemRepository;
    BillItemMapper billItemMapper;

    public BillItemResponse createBillItem(BillItemCreateRequest request) {
        return billItemMapper.toBillItemResponse(billItemRepository.save(billItemMapper.toBillItem(request)));
    }

    public BillItemResponse updateBillItem(String id, BillItemUpdateRequest request) {
        BillItem billItem = billItemRepository.findById(id).orElseThrow();
        billItemMapper.updateBillItem(billItem, request);
        return billItemMapper.toBillItemResponse(billItemRepository.save(billItem));
    }

    public List<BillItemResponse> getAllBillItems() {
        return billItemRepository.findAll().stream()
                .map(billItemMapper::toBillItemResponse)
                .toList();
    }

    public List<BillItemResponse> getBillItemsByBillId(String billId) {
        return billItemRepository.findByBillId(billId).stream()
                .map(billItemMapper::toBillItemResponse)
                .toList();
    }
}
