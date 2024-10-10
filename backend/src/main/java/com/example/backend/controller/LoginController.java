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

@RestController
@RequestMapping("/userLogin")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            if (user != null) {
                String token = JWTUtil.generateToken(user.getUsername());
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.badRequest().body("登录失败：用户名或密码错误");
            }
        } catch (Exception e) {
            // 这里可以记录日志，并返回一个更具体的错误信息
            return ResponseEntity.internalServerError().body("登录失败：服务器错误");
        }
    }
}
