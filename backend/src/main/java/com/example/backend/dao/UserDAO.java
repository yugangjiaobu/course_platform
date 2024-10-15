package com.example.backend.dao;

import com.example.backend.entity.User;
import java.util.List;

public interface UserDAO {
    void addUser(User user) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(String userId) throws Exception;
    User getUserById(String userId) throws Exception;
    List<User> getAllUsers() throws Exception;
}
