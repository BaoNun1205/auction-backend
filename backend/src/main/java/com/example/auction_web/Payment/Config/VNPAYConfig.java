package com.example.auction_web.Payment.Config;

import com.example.auction_web.utils.Payment.VNPayUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class VNPAYConfig {
    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;
    @Getter
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;
    @Value("${payment.vnPay.version}")
    private String vnp_Version;
    @Value("${payment.vnPay.command}")
    private String vnp_Command;
    @Value("${payment.vnPay.orderType}")
    private String orderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnPayparamsMap = new TreeMap<>();
        vnPayparamsMap.put("vnp_Version", vnp_Version);
        vnPayparamsMap.put("vnp_Command", vnp_Command);
        vnPayparamsMap.put("vnp_TmnCode", vnp_TmnCode);
        vnPayparamsMap.put("vnp_CurrCode", "VND");
        vnPayparamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
        vnPayparamsMap.put("vnp_OrderType", orderType);
        vnPayparamsMap.put("vnp_OrderInfo", "Thanh toán hóa đơn - " + VNPayUtil.getRandomNumber(8));
        vnPayparamsMap.put("vnp_Locale", "vn");
        vnPayparamsMap.put("vnp_ReturnUrl", vnp_ReturnUrl);

        // Lấy thời gian hiện tại theo VN
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // Thời gian tạo request
        String vnp_CreateDate = simpleDateFormat.format(calendar.getTime());
        vnPayparamsMap.put("vnp_CreateDate", vnp_CreateDate);

        // Thời gian hết hạn (15 phút sau)
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = simpleDateFormat.format(calendar.getTime());
        vnPayparamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

        return vnPayparamsMap;
    }

}
