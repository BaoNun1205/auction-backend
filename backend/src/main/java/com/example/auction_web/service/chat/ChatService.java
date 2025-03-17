package com.example.auction_web.service.chat;

import java.util.List;
import java.util.Map;

import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.chat.Message;

public interface ChatService {
    List<ConversationResponse> getConversations(String userId);
    List<MessageResponse> getMessages(String conversationId);
    Message sendMessage(String conversationId, Map<String, String> payload);
}
