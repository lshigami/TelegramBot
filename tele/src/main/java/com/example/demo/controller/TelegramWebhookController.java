package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.request.UserGroupRequest;
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
            String channelId = "-1002188041826"; // Channel ID
            // Save user if not exists
            userService.save(userMemberRequest);

            // If user is member of channel and user status is UNJOINED, add 100 points
            if (telegramService.isUserMemberOfChannel(userId, channelId) && userService.isUnjoined(userId)) {
                userService.addPoints(userId, 3000L);
                userService.makeUserJoined(userId);
                return APIResponse.builder().code(200).message("Success").data(new SubscriptionResponse("true", 3000L)).build();
            }
            // Check if user is member of channel : Prevent case buff points for user already joined
            else if(telegramService.isUserMemberOfChannel(userId, channelId) ) {
                return APIResponse.builder().code(201).message("User already joined").data(new SubscriptionResponse("false", 0L)).build();
                //If user is not a member of channel
            }else {
                return APIResponse.builder().code(202).message("User not joined").data(new SubscriptionResponse("false", 0L)).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.builder().code(500).message("Internal Server Error").data(new SubscriptionResponse("false", 0L)).build();
        }
    }

    @GetMapping("/channel/{id}")
    public APIResponse isJoinedChannel(@PathVariable String id) {
        boolean isJoined = telegramService.isUserMemberOfChannel(id, "-1002188041826");
        return APIResponse.builder().code(200).message(isJoined?"User is a member":"User is not a member").data(isJoined).build();
    }

    @PostMapping("/is_user_in_group")
    public APIResponse isUserInGroup(@RequestBody UserGroupRequest userGroupRequest) {
        boolean isJoined = userService.isUserInGroup(userGroupRequest.getId(), userGroupRequest.getGroup_id());
        return APIResponse.builder().code(200).message(isJoined?"User is a member":"User is not a member").data(isJoined).build();
    }

    @PostMapping("/telegram-webhook-group")
    public APIResponse handleUserJoinGroup(@RequestBody UserGroupRequest userMemberRequest) {
        boolean isJoined = telegramService.isUserMemberOfGroup(userMemberRequest.getId(), userMemberRequest.getGroup_id());
       if(isJoined) {
           return APIResponse.builder().code(200).message("User had joined group").data(isJoined).build();
       }
        return APIResponse.builder().code(202).message("User not joined").data(isJoined).build();
    }
}
