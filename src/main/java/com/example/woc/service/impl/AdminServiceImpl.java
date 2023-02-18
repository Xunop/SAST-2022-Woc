package com.example.woc.service.impl;

import com.example.woc.entity.User;
import com.example.woc.enums.ErrorEnum;
import com.example.woc.exception.LocalRuntimeException;
import com.example.woc.mapper.AdminMapper;
import com.example.woc.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xun
 * @create 2023/1/3 12:40
 */
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void delUser(String userName) {
        if (isValid(userName)) {
            adminMapper.delUser(userName);
        } else {
            throw new LocalRuntimeException(ErrorEnum.USERNAME_NOT_EXIST);
        }
    }

    @Override
    public Map<String, String> findByName(String userName) {
        User user= adminMapper.findByName(userName);
        if (user == null) throw new LocalRuntimeException(ErrorEnum.USERNAME_NOT_EXIST);
        return new HashMap<>() {{
            put("id", user.getId().toString());
            put("userName", user.getUserName());
            put("email", user.getEmail());
            put("role", user.getRole() == 0 ? "普通用户" : "管理员");
        }};
    }

    @Override
    public List<Map<String, String>> findAll(Integer pageNum, Integer pageSize) {
        List<Map<String, String>> result = new ArrayList<>();
        List<User> users = adminMapper.findAll((pageNum - 1) * pageSize, pageSize);
        Map<String, String> map = new HashMap<>();
        for (User user : users) {
            map.put("id", user.getId().toString());
            map.put("userName", user.getUserName());
            map.put("email", user.getEmail());
            map.put("role", user.getRole() == 0 ? "普通用户" : "管理员");
            result.add(new HashMap<>(map));
            map.clear();
        }
        return result;
    }

    /**
     * 判断用户名是否合法
     * @param userName 用户名
     * @return 如果用户名合法并且没有用户使用则返回 {@code true}
     */
    public Boolean isValid(String userName) {
        return !(userName == null || userName.isEmpty() || adminMapper.findByName(userName) != null);
    }
}
