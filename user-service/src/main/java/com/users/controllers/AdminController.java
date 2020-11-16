package com.users.controllers;

import com.users.entity.Users;
import com.users.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/v1/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping()
    public Users createSuperAdmin() {
      return adminService.createSuperAdmin();
    }
}
