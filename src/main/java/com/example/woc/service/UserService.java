package com.example.woc.service;

import com.example.woc.entity.User;

import java.util.Map;

/**
 * @Author xun
 * @create 2022/12/26 14:53
 */
public interface UserService {
    Boolean addUser(User user);

    Map<String, String> getUserInfo(User user);
    void editUserInfo(User newUser, User oldUser);
}
