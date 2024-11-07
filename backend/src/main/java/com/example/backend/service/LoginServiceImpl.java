package com.example.backend.service;

import com.example.backend.dao.UserDAO;
import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDAO userDAO;
    @Override
    public User login(String userID, String password) {
        try {
            User user = userDAO.getUserById(userID);
            // 直接进行明文密码比对，使用 Objects.equals 避免空指针异常
            if (user != null && Objects.equals(password, user.getPassword())) {
                return user; // 确保user包含角色信息
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息以便调试
            return null;
        }
    }

}