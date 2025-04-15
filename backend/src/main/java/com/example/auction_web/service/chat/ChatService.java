package com.example.auction_web.service.chat;

import java.util.List;
import java.util.Map;

import com.example.auction_web.dto.request.chat.ConversationRequest;
import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.chat.MessageResponse;

public interface ChatService {
    ConversationResponse createConversation(ConversationRequest request);
    List<ConversationResponse> getConversations(String userId);
    List<MessageResponse> getMessages(String conversationId);
    MessageResponse sendMessage(String conversationId, Map<String, String> payload);
    void updateUnread(String conversationId, int unreadCount);
}
