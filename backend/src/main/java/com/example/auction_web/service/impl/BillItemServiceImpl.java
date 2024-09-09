package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.BillItemResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.BillItem;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BillItemMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.BillItemRepository;
import com.example.auction_web.repository.BillRepository;
import com.example.auction_web.repository.DepositRepository;
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
    BillRepository billRepository;
    AssetRepository assetRepository;
    DepositRepository depositRepository;
    BillItemMapper billItemMapper;

    public BillItemResponse createBillItem(BillItemCreateRequest request) {
        var billItem = billItemMapper.toBillItem(request);
        setBillItemReference(billItem, request);
        return billItemMapper.toBillItemResponse(billItemRepository.save(billItem));
    }

    public BillItemResponse updateBillItem(String id, BillItemUpdateRequest request) {
        BillItem billItem = billItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_ITEM_NOT_EXISTED));
        billItemMapper.updateBillItem(billItem, request);
        return billItemMapper.toBillItemResponse(billItemRepository.save(billItem));
    }

    public List<BillItemResponse> getAllBillItems() {
        return billItemRepository.findAll().stream()
                .map(billItemMapper::toBillItemResponse)
                .toList();
    }

    public List<BillItemResponse> getBillItemsByBillId(String billId) {
        return billItemRepository.findBillItemsByBill_BillId(billId).stream()
                .map(billItemMapper::toBillItemResponse)
                .toList();
    }

    void setBillItemReference(BillItem billItem, BillItemCreateRequest request) {
        billItem.setBill(getBillById(request.getBillId()));
        billItem.setAsset(getAssetById(request.getAssetId()));
        billItem.setDeposit(getDepositById(request.getDepositId()));
    }

    Bill getBillById(String billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
    }

    Asset getAssetById(String assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
    }

    Deposit getDepositById(String depositId) {
        return depositRepository.findById(depositId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPOSIT_NOT_EXISTED));
    }
}
