package com.example.backend.service;

import com.example.backend.dao.UserDAO;
import com.example.backend.dto.UserInfoDTO;
import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserInfoDTO getUserInfo(String userId) throws Exception {
        User user = userDAO.getUserById(userId);
        return new UserInfoDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
    }

    @Override
    public boolean updateUserInfo(String userId, UserInfoDTO userInfo) throws Exception {
        User user = userDAO.getUserById(userId);
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getTel());
        userDAO.updateUser(user);
        return true;
    }
}
