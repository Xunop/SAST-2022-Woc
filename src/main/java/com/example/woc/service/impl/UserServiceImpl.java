package com.example.woc.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.woc.entity.User;
import com.example.woc.enums.ErrorEnum;
import com.example.woc.exception.LocalRuntimeException;
import com.example.woc.mapper.UserMapper;
import com.example.woc.service.UserService;
import com.example.woc.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xun
 * @create 2022/12/26 14:52
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private RedisUtil redisUtil;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
    @Override
    public Boolean addUser(User user) {
        if (!isValid(user.getUserName())) {
            throw new LocalRuntimeException(ErrorEnum.USERNAME_ERROR);
        } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new LocalRuntimeException(ErrorEnum.PASSWD_ERROR);
        }
        // 加密
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        userMapper.addUser(user);
        return true;
    }

    @Override
    public Map<String, String> getUserInfo(User user) {
        return new HashMap<>() {{
            put("userName", user.getUserName());
            put("email", user.getEmail());
        }};
    }

    @Override
    public void editUserInfo(User newUser, User oldUser) {
        Integer id = oldUser.getId();
        String newName = newUser.getUserName();
        String newEmail = newUser.getEmail();
        String newPasswd = newUser.getPassword();
        // 判断用户名是否需要更新
        if (newName == null || newName.isEmpty()) {
            newName = oldUser.getUserName();
        } else {
            if (!oldUser.getUserName().equals(newName) && !isValid(newName)) {
                throw new LocalRuntimeException(ErrorEnum.USERNAME_ERROR);
            }
        }
        // 判断是否需要更新email
        if (newEmail == null || newEmail.isEmpty()) {
            newEmail = oldUser.getEmail();
        }
        // 判断是否需要更新密码
        if (newPasswd == null || newPasswd.isEmpty()) {
            newPasswd = oldUser.getPassword();
        } else {
            newPasswd = DigestUtil.md5Hex(newPasswd);
            if (!newPasswd.equals(oldUser.getPassword())) {
                // 设置缓存过期时间
                redisUtil.expire("TOKEN:" + id, 1);
            }
        }
        newUser.setUserName(newName);
        newUser.setEmail(newEmail);
        newUser.setPassword(newPasswd);
        newUser.setId(id);
        userMapper.editUserInfo(newUser);
    }


    /**
     * 判断用户名是否合法
     * @param userName 用户名
     * @return 如果用户名合法并且没有用户使用则返回 {@code true}
     */
    public Boolean isValid(String userName) {
        User byName = userMapper.findByName(userName);
        if (userName == null || userName.isEmpty()) return false;
        return userMapper.findByName(userName) == null;
    }
}
