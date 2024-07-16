package com.example.demo.controller;


import com.example.demo.converter.UserConverter;
import com.example.demo.dto.request.UserGroupRequest;
import com.example.demo.dto.request.UserPointRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;

    @PostMapping
    public APIResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findTop100UsersByOrderByPointsDesc();
    }
    @GetMapping("/{id}")
    public Long getPoints(@PathVariable String id) {
        return userService.getPoints(id);
    }
    @GetMapping("/{id}/groups")
    public String getGroups(@PathVariable String id) {
        return userService.findById(id).getUserGroups();
    }
    @PostMapping("/groups")
    public APIResponse addGroup(@RequestBody UserGroupRequest userRequest) {
        userService.addGroup(userRequest.getId(), userRequest.getGroup_id());
        return APIResponse.builder().code(200).message("Success").data(userRequest).build();
    }
    @PostMapping("/points")
    public APIResponse addPoints(@RequestBody UserPointRequest userRequest) {
        userService.addPoints(userRequest.getId(), userRequest.getPoints());
        return APIResponse.builder().code(200).message("Success").data(userRequest).build();
    }

}
