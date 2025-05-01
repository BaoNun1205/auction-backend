package com.example.auction_web.ChatBot.Controller;

import com.example.auction_web.ChatBot.Dto.ProductRequest;
import com.example.auction_web.ChatBot.Service.ProductFilterService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-filter")
public class ProductFilterController {
    private final ProductFilterService productFilterService;

    @Autowired
    public ProductFilterController(ProductFilterService productFilterService) {
        this.productFilterService = productFilterService;
    }

    @PostMapping("/classify")
    public Map<String, String> classifyProduct(@RequestBody ProductRequest request) {
        return productFilterService.classifyProduct(
                request.getName(),
                request.getDescription(),
                request.getImageUrls()
        );
    }
}
