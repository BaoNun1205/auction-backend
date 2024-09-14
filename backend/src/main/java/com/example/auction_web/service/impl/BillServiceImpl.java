package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BillMapper;
import com.example.auction_web.repository.AddressRepository;
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
    DepositRepository depositRepository;
    AddressRepository addressRepository;
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

    public BillResponse findBillByDeposit_DepositId(String depositId) {
        if (!depositRepository.existsById(depositId)) {
            throw new AppException(ErrorCode.DEPOSIT_NOT_EXISTED);
        }
        return billMapper.toBillResponse(billRepository.findBillByDeposit_DepositId(depositId));
    }


    void setBillReference(Bill bill, BillCreateRequest request) {
        bill.setDeposit(getDepositById(request.getDepositId()));
        bill.setAddress(getAddressById(request.getAddressId()));
    }

    void setBillReference(Bill bill, BillUpdateRequest request) {
        bill.setDeposit(getDepositById(request.getDepositId()));
        bill.setAddress(getAddressById(request.getAddressId()));
    }

    Deposit getDepositById(String depositId) {
        return depositRepository.findById(depositId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPOSIT_NOT_EXISTED));
    }

    Address getAddressById(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
    }
}
