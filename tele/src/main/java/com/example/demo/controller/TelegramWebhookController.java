package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.request.UserMemberRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.dto.response.SubscriptionResponse;
import com.example.demo.entity.User;
import com.example.demo.service.TelegramService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TelegramWebhookController {
    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;

    @PostMapping("/telegram-webhook")
    public APIResponse handleUserJoinChannel(@RequestBody UserRequest userMemberRequest) {

        try {
            // Get user ID and channel ID
            String userId = userMemberRequest.getId();
            String channelId = "-1002232995211"; // Channel ID
            // Save user if not exists
            userService.save(userMemberRequest);

            // If user is member of channel and user status is UNJOINED, add 100 points
            if (telegramService.isUserMemberOfChannel(userId, channelId) && userService.isUnjoined(userId)) {
                userService.addPoints(userId, 100L);
                userService.makeUserJoined(userId);
                return APIResponse.builder().code(200).message("Success").data(new SubscriptionResponse("true", 100L)).build();
            }
            else if(telegramService.isUserMemberOfChannel(userId, channelId) ) {
                return APIResponse.builder().code(201).message("User already joined").data(new SubscriptionResponse("false", 0L)).build();
            }else {
                return APIResponse.builder().code(202).message("User not joined").data(new SubscriptionResponse("false", 0L)).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.builder().code(500).message("Internal Server Error").data(new SubscriptionResponse("false", 0L)).build();
        }
    }
    @PostMapping("/users")
    public APIResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findTop100UsersByOrderByPointsDesc();
    }
    @GetMapping("/users/{id}")
    public Long getPoints(@PathVariable String id) {
        return userService.getPoints(id);
    }

    @GetMapping("/channel/{id}")
    public boolean isJoinedChannel(@PathVariable String id) {
        return telegramService.isUserMemberOfChannel(id, "-1002232995211");
    }
}
