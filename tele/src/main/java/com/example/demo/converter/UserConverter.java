package com.example.demo.converter;

import com.example.demo.config.ModelMapperConfig;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public UserRequest convertToUserRequest(User user) {

        return modelMapper.map(user, UserRequest.class);
    }
}
