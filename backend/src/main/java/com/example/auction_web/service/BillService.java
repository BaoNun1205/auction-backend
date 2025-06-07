package com.example.auction_web.service;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;

import java.util.List;

public interface BillService {
    BillResponse createBill(BillCreateRequest request);
    BillResponse updateBill(String id, BillUpdateRequest request);
    List<BillResponse> getAllBills();
    List<BillResponse> getBillByBuyerBillId(String buyerBillId);
    List<BillResponse> getBillBySellerBillId(String sellerBillId);
    BillResponse getBillById(String id);
    BillResponse getBillBySessionId(String sessionId);
}
