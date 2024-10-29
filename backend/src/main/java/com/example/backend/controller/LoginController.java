package com.example.backend.controller;

import com.example.backend.dto.LoginDTO;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081") // 替换为你的前端地址
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            System.out.println("Received loginDTO: " + loginDTO);
            String userID = loginDTO.getUserID();
            String password = loginDTO.getPassword();

            User user = userService.login(userID, password);
            if (user != null) {
                String token = JWTUtil.generateToken(user.getName(), user.getRole()); // 传入角色
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.badRequest().body("登录失败：用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常以便调试
            return ResponseEntity.internalServerError().body("登录失败：服务器错误");
        }
    }
}


