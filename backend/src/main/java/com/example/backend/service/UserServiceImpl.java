package com.example.backend.service;

import com.example.backend.dao.UserDAO;
import com.example.backend.dto.UserInfoDTO;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.util.JWTUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Override
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return user; // 确保user包含角色信息
        }
        return null;
    }
    @Override
    public UserInfoDTO getUserInfo(String userId) throws Exception {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new Exception("用户不存在");
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUsername(user.getUserId());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setPhone(user.getPhone());
        return userInfoDTO;
    }
}
