package com.transquiz.transasker.controller;

import com.transquiz.transasker.dto.UserDto;
import com.transquiz.transasker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public String registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto.getUsername(), userDto.getPassword());
            return "Saved successfully";
        } catch (Exception e) {
            return "Not successfully";
        }
    }
}
