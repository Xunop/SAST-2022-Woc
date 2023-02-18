package com.example.woc.controller;

import com.example.woc.entity.User;
import com.example.woc.interceptor.UserInterceptor;
import com.example.woc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author xun
 * @create 2022/12/26 14:50
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public String addUser(User user) {
        return userService.addUser(user) ? "success" : "false";
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        User user = UserInterceptor.userHolder.get();
        return userService.getUserInfo(user);
    }

    @PostMapping("/edit_info")
    public String editInfo(User user) {
        User curUser = UserInterceptor.userHolder.get();
        userService.editUserInfo(user, curUser);
        return "success";
    }

}
