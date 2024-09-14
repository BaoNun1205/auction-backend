package com.example.auction_web.service;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.auth.User;

import java.util.List;

public interface BillService {
    BillResponse createBill(BillCreateRequest request);
    BillResponse updateBill(String id, BillUpdateRequest request);
    List<BillResponse> getAllBills();
    BillResponse findBillByDeposit_DepositId(String depositId);
}
