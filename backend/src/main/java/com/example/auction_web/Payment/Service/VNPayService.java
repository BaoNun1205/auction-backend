package com.example.auction_web.Payment.Service;

import com.example.auction_web.Payment.Config.VNPAYConfig;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.utils.Payment.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class VNPayService {
    private final VNPAYConfig vnPayConfig;
    public VNPayDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        String userId = request.getParameter("userId");

        String txnRef = userId + "/" + System.currentTimeMillis();

        Map<String, String> vnpParamsMap = new TreeMap<>(vnPayConfig.getVNPayConfig());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_TxnRef", txnRef);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toán nạp tiền - " + System.currentTimeMillis());

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);

        vnpParamsMap.put("vnp_SecureHash", vnpSecureHash);

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + VNPayUtil.getPaymentURL(vnpParamsMap, true);

        return VNPayDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

}
