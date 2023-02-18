package com.example.woc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author NuoTian
 * @date 2022/7/25
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    TOURIST(-1, "游客"),
    COMMON_USER(0, "普通用户"),
    ADMIN(1, "管理员");

    private final Integer role;
    private final String roleName;
}
