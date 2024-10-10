package com.example.backend.dao;

import com.example.backend.entity.User;

public interface UserDAO {
    User findByUsername(String username);
}
