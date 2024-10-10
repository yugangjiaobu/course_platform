package com.example.backend.service;

import com.example.backend.dao.UserDAO;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
