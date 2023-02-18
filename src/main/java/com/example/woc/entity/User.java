package com.example.woc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xun
 * @create 2022/12/26 14:31
 */
// Lombok 的注解：不需要我们写构造器和 Getter Setter 方法。
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private Integer role;
}
