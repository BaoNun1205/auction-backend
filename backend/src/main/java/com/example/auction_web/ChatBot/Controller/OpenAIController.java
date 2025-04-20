package com.example.auction_web.ChatBot.Controller;

import com.example.auction_web.ChatBot.Service.OpenAIService;
import com.example.auction_web.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/test-tool-call")
    public ResponseEntity<?> testChatWithToolCall(@RequestBody String messages) {
        try {
            var response = openAIService.chatWithToolCalls(messages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
