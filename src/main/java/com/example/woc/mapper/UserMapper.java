package com.example.woc.mapper;

import com.example.woc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author xun
 * @create 2022/12/26 14:47
 */
@Mapper
public interface UserMapper {
    void addUser(User user);
    User findByName(@Param("userName") String userName);
    User findById(@Param("id") Integer id);
    void editUserInfo(User user);
}
