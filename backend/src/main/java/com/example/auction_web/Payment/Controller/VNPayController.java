package com.example.auction_web.Payment.Controller;

import com.example.auction_web.Payment.Dto.ResponseObject;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.Payment.Service.VNPayService;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.service.BalanceUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;
    private final BalanceUserService balanceUserService;

    @GetMapping("/vn-pay")
    public ResponseObject<VNPayDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", vnPayService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String txnRef = request.getParameter("vnp_TxnRef");
        String[] parts = txnRef.split("/");
        String userId = parts[0];
        String status = request.getParameter("vnp_ResponseCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String amountStr = request.getParameter("vnp_Amount");
        BigDecimal amount = new BigDecimal(amountStr).divide(new BigDecimal(100));

        if ("00".equals(status)) {
            balanceUserService.updateCoinUserVnPay(userId, orderInfo, amount);
            response.sendRedirect("http://localhost:5173/payment/vn-pay-callback?status=success&amount=" + amount);
        } else {
            response.sendRedirect("http://localhost:5173/payment/vn-pay-callback?status=fail");
        }
    }
}
