package com.example.auction_web.utils.Job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.auth.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResponseTimeScheduler {

    UserRepository userRepository;
    UserService userService;

    @Scheduled(cron = "0 0 0 * * *") 

    public void scheduledUpdateResponseTimes() {
        List<User> users = userRepository.findAll(); 

        for (User user : users) {
            userService.updateUserAverageResponseTime(user);
            userService.updateUserResponseRate(user);  
        }
    }
}
