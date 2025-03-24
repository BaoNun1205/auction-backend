package com.example.auction_web.service.chat.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.chat.Conversation;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.mapper.ConversationMapper;
import com.example.auction_web.mapper.MessageMapper;
import com.example.auction_web.repository.chat.ConversationRepository;
import com.example.auction_web.repository.chat.MessageRepository;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.chat.ChatService;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<ConversationResponse> getConversations(String userId) {
        List<Conversation> conversations = conversationRepository.findConversationsByBuyer_UserIdOrSeller_UserId(userId, userId);
        return conversations.stream()
                .map(conversationMapper::toConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getMessages(String conversationId) {
        List<Message> messages = messageRepository.findMessageByConversationId(conversationId);
        return messages.stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MessageResponse sendMessage(String conversationId, Map<String, String> payload) {
        // Tìm conversation và sender
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userService.getUser(payload.get("senderId"));
        // Tạo message mới
        Message message = new Message();
        message.setContent(payload.get("content"));
        message.setTimestamp(LocalDateTime.now().toString());
        message.setConversationId(conversation.getConversationId());
        message.setSender(sender);
        // Cập nhật conversation
        conversation.setLastMessage(payload.get("content"));
        conversation.setTime(LocalDateTime.now().toString());
        if (conversation.getBuyer().getUserId().equals(sender.getUserId())) {
            conversation.setUnread(conversation.getUnread() + 1); // Tăng unread cho seller
        } else if (conversation.getSeller().getUserId().equals(sender.getUserId())) {
            conversation.setUnread(conversation.getUnread() + 1); // Tăng unread cho buyer
        }
        conversationRepository.save(conversation);

        return messageMapper.toMessageResponse(messageRepository.save(message));
    }

    @Transactional
    @Override
    public void updateUnread(String conversationId, int unreadCount) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        conversation.setUnread(unreadCount);
        conversationRepository.save(conversation);
    }
}
