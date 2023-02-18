package com.example.woc.mapper;

import com.example.woc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author xun
 * @create 2023/1/3 12:38
 */
@Mapper
public interface AdminMapper {
    void delUser(@Param("userName") String userName);

    User findByName(@Param("userName") String userName);

    List<User> findAll(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
}
