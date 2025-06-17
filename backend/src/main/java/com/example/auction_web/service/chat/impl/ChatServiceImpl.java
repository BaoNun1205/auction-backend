package com.example.auction_web.service.chat.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.chat.ConversationRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.chat.Conversation;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.ConversationMapper;
import com.example.auction_web.mapper.MessageMapper;
import com.example.auction_web.repository.chat.ConversationRepository;
import com.example.auction_web.repository.chat.MessageRepository;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.chat.ChatService;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ChatServiceImpl implements ChatService {
    ConversationRepository conversationRepository;
    MessageRepository messageRepository;
    UserService userService;
    ConversationMapper conversationMapper;
    MessageMapper messageMapper;
    NotificationStompService notificationStompService;

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
        conversation.setUpdatedAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        MessageResponse savedMessage = messageMapper.toMessageResponse(messageRepository.save(message));

        return savedMessage;
    }

    @Transactional
    @Override
    public void updateUnread(String conversationId, int unreadCount) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        conversation.setUnread(unreadCount);
        conversationRepository.save(conversation);
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest request) {
        try {
            String userId1 = request.getBuyerId();
            String userId2 = request.getSellerId();

            Optional<Conversation> existingConversation = conversationRepository
                    .findConversationBetweenUsers(userId1, userId2);

            if (existingConversation.isPresent()) {
                return conversationMapper.toConversationResponse(existingConversation.get());
            }

            // Nếu không có conversation nào giữa hai người, tạo một conversation mới
            User buyer = userService.getUser(request.getBuyerId());
            User seller = userService.getUser(request.getSellerId());
    
            Conversation conversation = new Conversation();
            conversation.setBuyer(buyer);
            conversation.setSeller(seller);
            conversation.setName(buyer.getUsername() + " - " + seller.getUsername());
            conversation.setLastMessage("");
            conversation.setTime(LocalDateTime.now().toString());
            conversation.setUnread(0);
    
            Conversation savedConversation = conversationRepository.save(conversation);
    
            return conversationMapper.toConversationResponse(savedConversation);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_CONVERSATION_FAILED);
        }
    }    
}
