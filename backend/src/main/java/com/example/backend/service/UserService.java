package com.example.backend.service;

import com.example.backend.dto.UserInfoDTO;

public interface UserService {
    UserInfoDTO getUserInfo(String userId) throws Exception;
    boolean updateUserInfo(String userId, UserInfoDTO userInfo) throws Exception;
}
