package com.example.woc.controller;

import com.example.woc.annotation.CheckRole;
import com.example.woc.enums.UserRoleEnum;
import com.example.woc.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author xun
 * @create 2022/12/26 14:49
 */
@CheckRole(UserRoleEnum.ADMIN)
@RestController
@RequestMapping("/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/del_user")
    public String delUser(String userName) {
        adminService.delUser(userName);
        return "success";
    }

    @GetMapping("/find_user_info")
    public Map<String, String> findUser(String userName) {
        return adminService.findByName(userName);
    }

    @GetMapping("/show_all")
    public List<Map<String, String>> showUsers(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        return adminService.findAll(pageNum, pageSize);
    }
}
