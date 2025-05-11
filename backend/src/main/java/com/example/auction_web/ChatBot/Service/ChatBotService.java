package com.example.auction_web.ChatBot.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.auction_web.ChatBot.Dto.MessageCreateRequestDto;
import org.springframework.stereotype.Service;

import com.example.auction_web.ChatBot.Dto.BotConversationResponse;
import com.example.auction_web.ChatBot.Dto.BotMessageResponse;
import com.example.auction_web.ChatBot.Entity.BotConversation;
import com.example.auction_web.ChatBot.Entity.BotMessage;
import com.example.auction_web.ChatBot.Enum.Role;
import com.example.auction_web.ChatBot.Mapper.BotConversationMapper;
import com.example.auction_web.ChatBot.Mapper.BotMessageMapper;
import com.example.auction_web.ChatBot.Repository.BotConversationRepository;
import com.example.auction_web.ChatBot.Repository.BotMessageRepository;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.repository.chat.ConversationRepository;
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
    BotConversationRepository conversationRepository;

    public List<BotConversationResponse> getConversations(String userId) {
        List<BotConversation> conversations = botConversationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
        return conversations.stream()
                .map(botConversationMapper::toConversationResponse)
                .collect(Collectors.toList());
    }

    public void createMessage(MessageCreateRequestDto request) {
        botMessageRepository.save(botMessageMapper.toMessage(request));
        long messageCount = botMessageRepository.countByConversationId(request.getConversationId());

        if (messageCount == 1 && request.getRole() == Role.user) {
            conversationRepository.findById(request.getConversationId()).ifPresent(conversation -> {
                conversation.setTopic(request.getContent());
                conversationRepository.save(conversation);
            });
        }
    }


    public List<BotMessageResponse> getMessages(String conversationId) {
        List<BotMessage> messages = botMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(botMessageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public BotConversationResponse createConversation(String userId) {
        try {
            User user = userService.getUser(userId);
    
            BotConversation conversation = new BotConversation();
            conversation.setUser(user);
            conversation.setTopic("Đoạn chat mới");
    
            BotConversation saved = botConversationRepository.save(conversation);
            return botConversationMapper.toConversationResponse(saved);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_BOT_CONVERSATION_FAILED);
        }
    }
}

