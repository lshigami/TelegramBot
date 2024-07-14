package com.example.demo.service;

import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String botToken;
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    public boolean isUserMemberOfChannel(String userId, String channelId) {
        String url = TELEGRAM_API_URL + botToken + "/getChatMember?chat_id=" + channelId + "&user_id=" + userId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String ok = root.path("ok").asText();
            if(ok.equals("false") || root.path("result").path("status").equals("left")) {
                return false;
            }
            String status = root.path("result").path("status").asText();
            return "member".equals(status) || "administrator".equals(status) || "creator".equals(status);
        } catch (Exception e) {
            System.out.println("Error in TelegramService.isUserMemberOfChannel");
            e.printStackTrace();
            return false;
        }

    }




}

