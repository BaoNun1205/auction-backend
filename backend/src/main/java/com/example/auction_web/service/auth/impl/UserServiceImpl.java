package com.example.auction_web.service.auth.impl;

import com.example.auction_web.constant.PredefinedRole;
import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.request.auth.UserUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.Role;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.chat.Conversation;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.repository.auth.RoleRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.repository.chat.ConversationRepository;
import com.example.auction_web.repository.chat.MessageRepository;
import com.example.auction_web.service.EmailVerificationTokenService;
import com.example.auction_web.service.FileUploadService;
import com.example.auction_web.service.auth.UserService;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FileUploadService fileUploadService;
    MessageRepository messageRepository;
    ConversationRepository conversationRepository;

    EmailVerificationTokenService emailVerificationTokenService;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUer(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEnabled(false);

        try {
            user = userRepository.save(user);
            emailVerificationTokenService.sendEmailConfirmation(request.getEmail());
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserResponse(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public UserResponse getUserByUsername(String username) {
        return userMapper.toUserResponse(
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    public UserResponse getUserByEmail(String email) {
        return userMapper.toUserResponse(
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    public void updateAvatar(String userId, MultipartFile image) {
        User user = getUser(userId);
        try {
            String imageUrl = fileUploadService.uploadFile(image);
            user.setAvatar(imageUrl);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + image.getOriginalFilename(), e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
   @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            try {
                String imageUrl = fileUploadService.uploadFile(request.getAvatar());
                user.setAvatar(imageUrl);
                userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + request.getAvatar().getOriginalFilename(), e);
            }
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }


    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUnreadNotificationCount(String userId, Long count) {
        User user = getUser(userId);
        user.setUnreadNotificationCount(count);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserAverageResponseTime(User user) {
        if (user == null) return;
    
        // 1. Lấy tất cả conversation mà user tham gia
        List<Conversation> conversations = conversationRepository.findConversationsByBuyerOrSeller(user, user);
        if (conversations.isEmpty()) return;
    
        List<String> conversationIds = conversations.stream()
                .map(Conversation::getConversationId)
                .toList();
    
        // 2. Xác định thời điểm bắt đầu lấy message mới
        LocalDateTime lastCalculatedAt = user.getLastRespontimeCalculatedAt();
        if (lastCalculatedAt == null) {
            // Nếu chưa từng tính bao giờ thì lấy từ quá khứ xa
            lastCalculatedAt = LocalDateTime.of(2000, 1, 1, 0, 0); 
        }
    
        // 3. Query tất cả opponent messages mới phát sinh
        List<Message> opponentMessages = messageRepository.findAllByConversationIdInAndSender_UserIdNotAndCreatedAtAfter(
                conversationIds, user.getUserId(), lastCalculatedAt
        );
        if (opponentMessages.isEmpty()) return;
    
        // 4. Query tất cả reply mới của user
        List<Message> userReplies = messageRepository.findAllByConversationIdInAndSender_UserIdAndCreatedAtAfter(
                conversationIds, user.getUserId(), lastCalculatedAt
        );
        if (userReplies.isEmpty()) return;
    
        // 5. Map replies
        userReplies.sort(Comparator.comparing(Message::getConversationId).thenComparing(Message::getCreatedAt));
    
        List<Long> newResponseTimes = new ArrayList<>();
    
        for (Message opponentMessage : opponentMessages) {
            Optional<Message> replyOpt = userReplies.stream()
                    .filter(reply -> reply.getConversationId().equals(opponentMessage.getConversationId())
                            && reply.getCreatedAt().isAfter(opponentMessage.getCreatedAt()))
                    .findFirst();
    
            if (replyOpt.isPresent()) {
                Message userReply = replyOpt.get();
                long seconds = Duration.between(opponentMessage.getCreatedAt(), userReply.getCreatedAt()).getSeconds();
                if (seconds > 0) {
                    newResponseTimes.add(seconds);
                }
            }
        }
    
        // 6. Cập nhật dữ liệu response time
        if (!newResponseTimes.isEmpty()) {
            long sumNewTimes = newResponseTimes.stream().mapToLong(Long::longValue).sum();
            long newCount = newResponseTimes.size();
    
            Long oldTotalCount = user.getTotalResponseCount() != null ? user.getTotalResponseCount() : 0L;
            Long oldAverageTime = user.getResponseTimeInSeconds() != null ? user.getResponseTimeInSeconds() : 0L;
    
            long oldTotalTime = oldAverageTime * oldTotalCount;
    
            long newTotalTime = oldTotalTime + sumNewTimes;
            long updatedTotalCount = oldTotalCount + newCount;
            long updatedAverageTime = newTotalTime / updatedTotalCount;
    
            user.setTotalResponseCount(updatedTotalCount);
            user.setResponseTimeInSeconds(updatedAverageTime);
            user.setLastRespontimeCalculatedAt(LocalDateTime.now()); // Cập nhật thời điểm tính cuối cùng
    
            userRepository.save(user);
        }
    }
    
    @Transactional
    @Override
    public void updateUserResponseRate(User user) {
        if (user == null) {
            return;
        }

        List<Conversation> conversations = conversationRepository.findConversationsByBuyerOrSeller(user, user);
        if (conversations.isEmpty()) {
            return;
        }

        List<String> conversationIds = conversations.stream()
                .map(Conversation::getConversationId)
                .toList();

        LocalDateTime lastCalculatedAt = user.getLastResponRateCalculatedAt();
        if (lastCalculatedAt == null) {
            lastCalculatedAt = LocalDateTime.of(2000, 1, 1, 0, 0);
        }

        List<Message> opponentMessages = messageRepository.findAllByConversationIdInAndSender_UserIdNotAndCreatedAtAfter(
                conversationIds, user.getUserId(), lastCalculatedAt
        );
        if (opponentMessages.isEmpty()) {
            return;
        }

        List<Message> userReplies = messageRepository.findAllByConversationIdInAndSender_UserIdAndCreatedAtAfter(
                conversationIds, user.getUserId(), lastCalculatedAt
        );
        if (userReplies.isEmpty()) {
            return;
        }

        userReplies.sort(Comparator.comparing(Message::getConversationId).thenComparing(Message::getCreatedAt));

        long respondedCount = 0L;

        for (Message opponentMessage : opponentMessages) {
            Optional<Message> replyOpt = userReplies.stream()
                    .filter(userReply -> userReply.getConversationId().equals(opponentMessage.getConversationId())
                            && userReply.getCreatedAt().isAfter(opponentMessage.getCreatedAt()))
                    .findFirst();

            if (replyOpt.isPresent()) {
                respondedCount++;
            }
        }

        long totalNewOpponentMessages = opponentMessages.size();
        long oldTotalOpponentMessages = user.getTotalOpponentMessages() != null ? user.getTotalOpponentMessages() : 0L;
        long oldTotalRespondedMessages = user.getTotalOpponentMessagesReplied() != null ? user.getTotalOpponentMessagesReplied() : 0L;

        user.setTotalOpponentMessages(oldTotalOpponentMessages + totalNewOpponentMessages);
        user.setTotalOpponentMessagesReplied(oldTotalRespondedMessages + respondedCount);

        if (user.getTotalOpponentMessages() > 0) {
            double responseRate = (user.getTotalOpponentMessagesReplied() * 100.0) / user.getTotalOpponentMessages();
            user.setResponseRate((double) Math.round(responseRate));
        } else {
            user.setResponseRate(0.0);
        }        

        user.setLastResponRateCalculatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}
