package com.example.demo.service;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void addPoints(String id, Long points) {
        User user = userRepository.findById(id).orElse(null);
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }
    public Long getPoints(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user.getPoints();
    }
    public boolean isUnjoined(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user.getStatus().equals("UNJOINED");
    }

    public APIResponse save(UserRequest userRequest) {
        String id=userRequest.getId();
        if(!userRepository.existsById(id)) {
            userRepository.save(User.builder().id(id).points(0L).status("UNJOINED").first_name(userRequest.getFirst_name()).last_name(userRequest.getLast_name()).build());
            return APIResponse.builder().code(200).message("Success").data(userRequest).build();
        }
        return APIResponse.builder().code(400).message("User already exists").data(null).build();
    }


    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void makeUserJoined(String id) {
        User user = userRepository.findById(id).orElse(null);
        user.setStatus("JOINED");
        userRepository.save(user);
    }

    public List<User> findTop100UsersByOrderByPointsDesc() {
        return userRepository.findTop100UsersByPoints();
    }


}
