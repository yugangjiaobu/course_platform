package com.example.backend.service;

import com.example.backend.entity.User;

public interface LoginService {
    User login(String userID, String password);
}