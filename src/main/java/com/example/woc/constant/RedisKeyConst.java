package com.example.woc.constant;


/**
 * @Author xun
 * @create 2023/1/3 13:52
 */
public class RedisKeyConst {

    public static String getTokenKey(Integer id) {
        return "TOKEN:" + id;
    }
}
