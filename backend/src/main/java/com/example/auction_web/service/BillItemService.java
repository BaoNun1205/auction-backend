package com.example.auction_web.service;

import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.BillItemResponse;

import java.util.List;

public interface BillItemService {
    BillItemResponse createBillItem(BillItemCreateRequest request);
    BillItemResponse updateBillItem(String id, BillItemUpdateRequest request);
    List<BillItemResponse> getAllBillItems();
    List<BillItemResponse> getBillItemsByBillId(String billId);
}
