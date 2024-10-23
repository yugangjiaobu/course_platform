package com.example.backend.service;

import com.example.backend.dto.UserInfoDTO;
import com.example.backend.entity.User;

public interface UserService {
    User login(String userID, String password);
    UserInfoDTO getUserInfo(String userId) throws Exception;

}
