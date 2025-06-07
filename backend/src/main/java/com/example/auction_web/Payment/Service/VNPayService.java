package com.example.auction_web.Payment.Service;

import com.example.auction_web.Payment.Config.VNPAYConfig;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.Payment.Dto.VNPayRequestDTO;
import com.example.auction_web.utils.IpUtils;
import com.example.auction_web.utils.Payment.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class VNPayService {
    private final VNPAYConfig vnPayConfig;
    public VNPayDTO.VNPayResponse createVnPayPayment(VNPayRequestDTO dto, String ipAddress) {
        try {
            long amount = Long.valueOf(dto.getAmount()) * 100L;
            String bankCode = dto.getBankCode();
            String userId = dto.getUserId();

            if (userId == null) {
                userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            String txnRef = userId + "/" + System.currentTimeMillis();

            Map<String, String> vnpParamsMap = new TreeMap<>(vnPayConfig.getVNPayConfig());
            vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
            vnpParamsMap.put("vnp_TxnRef", txnRef);

            String orderInfo = "Thanh toán nạp tiền - " + System.currentTimeMillis();
            String encodedOrderInfo = URLEncoder.encode(orderInfo, StandardCharsets.UTF_8.toString());
            vnpParamsMap.put("vnp_OrderInfo", encodedOrderInfo);

            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParamsMap.put("vnp_BankCode", bankCode);
            }

            if (ipAddress == null || ipAddress.isBlank()) {
                ipAddress = IpUtils.getServerIpAddress();
            }
            vnpParamsMap.put("vnp_IpAddr", ipAddress);

            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            vnpParamsMap.put("vnp_SecureHash", vnpSecureHash);

            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + VNPayUtil.getPaymentURL(vnpParamsMap, true);

            return VNPayDTO.VNPayResponse.builder()
                    .code("ok")
                    .message("success")
                    .paymentUrl(paymentUrl)
                    .build();
        } catch (Exception e) {
            // Trả về response lỗi
            return VNPayDTO.VNPayResponse.builder()
                    .code("error")
                    .message("Failed to create VNPay payment: " + e.getMessage())
                    .paymentUrl(null)
                    .build();
        }
    }
}
