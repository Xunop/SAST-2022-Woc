package com.example.woc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author xun
 * @create 2022/12/26 15:25
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {
    COMMON_ERROR(1000, "错误"),
    TOKEN_ERROR(1001, "TOKEN错误"),
    NO_TOKEN(1002, "TOKEN为空"),
    NO_LOGIN(1003, "没有登录"),
    NO_ROLE(1004, "无权限"),
    EXPIRED_LOGIN(1005, "登录过期"),
    USERNAME_ERROR(2001, "用户名设置错误"),
    PASSWD_ERROR(2002, "密码设置错误"),
    USERNAME_NOT_EXIST(2003, "用户名不存在");
    private final Integer errCode;
    private final String errMsg;

}
