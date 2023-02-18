package com.example.woc.annotation;

import com.example.woc.enums.UserRoleEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author xun
 * @create 2023/1/3 14:21
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    @AliasFor("role")
    UserRoleEnum value() default UserRoleEnum.COMMON_USER;
    @AliasFor("value")
    UserRoleEnum role() default UserRoleEnum.COMMON_USER;
}

