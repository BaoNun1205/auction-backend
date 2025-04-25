package com.example.auction_web.ChatBot.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.auction_web.ChatBot.Dto.BotConversationResponse;
import com.example.auction_web.ChatBot.Dto.BotMessageResponse;
import com.example.auction_web.ChatBot.Entity.BotConversation;
import com.example.auction_web.ChatBot.Entity.BotMessage;
import com.example.auction_web.ChatBot.Enum.SenderType;
import com.example.auction_web.ChatBot.Mapper.BotConversationMapper;
import com.example.auction_web.ChatBot.Mapper.BotMessageMapper;
import com.example.auction_web.ChatBot.Repository.BotConversationRepository;
import com.example.auction_web.ChatBot.Repository.BotMessageRepository;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.service.auth.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatBotService {
    BotConversationRepository botConversationRepository;
    BotMessageRepository botMessageRepository;
    UserService userService;
    BotConversationMapper botConversationMapper;
    BotMessageMapper botMessageMapper;

    public List<BotConversationResponse> getConversations(String userId) {
        List<BotConversation> conversations = botConversationRepository.findByUser_UserId(userId);
        return conversations.stream()
                .map(botConversationMapper::toConversationResponse)
                .collect(Collectors.toList());
    }

    public void createMessage(String conversationId, SenderType sender, String content) {
        BotMessage message = BotMessage.builder()
                .conversationId(conversationId)
                .sender(sender)
                .content(content)
                .build();

        botMessageRepository.save(message);
    }


    public List<BotMessageResponse> getMessages(String conversationId) {
        List<BotMessage> messages = botMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(botMessageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public BotConversationResponse createConversation(String userId) {
        User user = userService.getUser(userId);

        // Mỗi lần tạo là 1 chủ đề chatbot mới (không check tồn tại)
        BotConversation conversation = new BotConversation();
        conversation.setUser(user);
        conversation.setTopic("Chat with AI - " + LocalDateTime.now());

        BotConversation saved = botConversationRepository.save(conversation);
        return botConversationMapper.toConversationResponse(saved);
    }
}

