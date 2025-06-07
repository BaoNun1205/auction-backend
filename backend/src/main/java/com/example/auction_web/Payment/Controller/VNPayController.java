package com.example.auction_web.Payment.Controller;

import com.example.auction_web.Payment.Dto.ResponseObject;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.Payment.Dto.VNPayRequestDTO;
import com.example.auction_web.Payment.Service.VNPayService;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.service.BalanceUserService;
import com.example.auction_web.utils.Payment.VNPayUtil;
import com.example.auction_web.utils.decodeUTF8Param;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;
    private final BalanceUserService balanceUserService;

    @GetMapping("/vn-pay")
    public ResponseObject<VNPayDTO.VNPayResponse> pay(HttpServletRequest request) {
        VNPayRequestDTO requestDTO = VNPayRequestDTO.builder()
                .bankCode(request.getParameter("bankCode"))
                .userId(request.getParameter("userId"))
                .amount(request.getParameter("amount"))
                .build();
        String ipAddress = VNPayUtil.getIpAddress(request);
        return new ResponseObject<>(HttpStatus.OK, "Success", vnPayService.createVnPayPayment(requestDTO, ipAddress));
    }

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String txnRef = request.getParameter("vnp_TxnRef");
        String[] parts = txnRef.split("/");
        String userId = parts[0];
        String status = request.getParameter("vnp_ResponseCode");
        request.setCharacterEncoding("UTF-8");
        String encodedOrderInfo = request.getParameter("vnp_OrderInfo");
        String orderInfo = URLDecoder.decode(encodedOrderInfo, StandardCharsets.UTF_8.toString());

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
