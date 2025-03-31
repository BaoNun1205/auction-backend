package com.example.auction_web.Payment.Controller;

import com.example.auction_web.Payment.Dto.ResponseObject;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.Payment.Service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;

    @GetMapping("/vn-pay")
    public ResponseObject<VNPayDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", vnPayService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseObject<VNPayDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseObject<>(HttpStatus.OK, "Success", new VNPayDTO.VNPayResponse("00", "Success", ""));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }
}
