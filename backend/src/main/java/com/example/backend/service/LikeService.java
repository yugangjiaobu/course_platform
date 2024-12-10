package com.example.backend.service;

import com.example.backend.dto.*;

import java.sql.SQLException;
import java.util.List;

public interface LikeService {
    List<LikedPostDTO> getLikedPosts(String userId) throws Exception;
    List<UserPostDTO> getUserPosts(String userId) throws SQLException;
}
