package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BillMapper;
import com.example.auction_web.repository.AddressRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.BillRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.repository.auth.UserRepository;
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
    AddressRepository addressRepository;
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    BillMapper billMapper;

    public BillResponse createBill(BillCreateRequest request) {
        var bill = billMapper.toBill(request);
        setBillReference(bill, request);
        return billMapper.toBillResponse(billRepository.save(bill));
    }

    public BillResponse updateBill(String id, BillUpdateRequest request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
        billMapper.updateBill(bill, request);
        setBillReference(bill, request);
        return billMapper.toBillResponse(billRepository.save(bill));
    }

    public List<BillResponse> getAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    public List<BillResponse> getBillByBuyerBillId(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return billRepository.findBillsByBuyerBill_UserId(userId).stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    public List<BillResponse> getBillBySellerBillId(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return billRepository.findBillsBySellerBill_UserId(userId).stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    public BillResponse getBillById(String id) {
        var bill = billRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
        return billMapper.toBillResponse(bill);
    }


    void setBillReference(Bill bill, BillCreateRequest request) {
        bill.setAddress(getAddressById(request.getAddressId()));
        bill.setSellerBill(getUser(request.getSellerId()));
        bill.setBuyerBill(getUser(request.getBuyerId()));
        bill.setSession(getAuctionSession(request.getSessionId()));
    }

    void setBillReference(Bill bill, BillUpdateRequest request) {
        bill.setAddress(getAddressById(request.getAddressId()));
    }

    Address getAddressById(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
    }

    AuctionSession getAuctionSession(String auctionSession) {
        return auctionSessionRepository.findById(auctionSession)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }

    // get user
    User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public BillResponse getBillBySessionId(String sessionId) {
        Bill bill = billRepository.findBillBySession_AuctionSessionId(sessionId);
        if (bill == null) {
            throw new AppException(ErrorCode.BILL_NOT_EXISTED);
        }
        return billMapper.toBillResponse(bill);
    }
}
