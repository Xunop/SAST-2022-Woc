package com.example.woc.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.woc.annotation.PassToken;
import com.example.woc.constant.RedisKeyConst;
import com.example.woc.entity.User;
import com.example.woc.exception.LocalRuntimeException;
import com.example.woc.mapper.UserMapper;
import com.example.woc.util.JWTUtil;
import com.example.woc.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author xun
 * @create 2022/12/26 14:49
 */
@Slf4j
@RestController
public class LoginController {

    private UserMapper userMapper;
    private JWTUtil jwtUtil;
    private RedisUtil redisUtil;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Autowired
    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
    @PassToken
    @PostMapping("/login")
    public Map<String, Object> login(@RequestHeader("User-Agent") String agent,
                                     @RequestParam(defaultValue = "") String userName,
                                     @RequestParam(defaultValue = "") String password) {
        if ("".equals(userName) || "".equals(password)) {
            throw new LocalRuntimeException("用户名或密码不能为空");
        }

        User userFromDB = userMapper.findByName(userName);
        if (userFromDB == null) {
            throw new LocalRuntimeException("账号不存在");
        } else if (!DigestUtil.md5Hex(password).equals(userFromDB.getPassword())) {
            throw new LocalRuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(userFromDB);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userFromDB.getUserName());
        map.put("id", userFromDB.getId());
        map.put("email", userFromDB.getEmail() == null ? "无" : userFromDB.getEmail());
        map.put("role", userFromDB.getRole());
        map.put("token", token);

        log.info("===============================================");
        log.info("用户登录：{}，role：{}", userFromDB.getUserName(), userFromDB.getRole());
        log.info("登录Agent:{}", agent);
        log.info("===============================================");

        //用redis中的过期时间代替JWT的过期时间，每次经过拦截器时更新过期时间
        redisUtil.set(RedisKeyConst.getTokenKey(userFromDB.getId()), token, 1, TimeUnit.HOURS);
        return map;
    }
}
