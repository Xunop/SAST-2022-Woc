package com.example.woc.service;


import java.util.List;
import java.util.Map;

/**
 * @Author xun
 * @create 2023/1/3 12:40
 */
public interface AdminService {
    void delUser(String userName);
    Map<String, String> findByName(String userName);
    List<Map<String, String>> findAll(Integer pageNum, Integer pageSize);
}
