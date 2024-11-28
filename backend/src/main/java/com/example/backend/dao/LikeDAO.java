package com.example.backend.dao;

import com.example.backend.entity.Like;

import java.util.List;

public interface LikeDAO {
    void save(Like like);
    Like findById(String likeId);
    void update(Like like);
    void delete(Like like);
    List<Like> findAll();

    int countLikesByPostId(String postId);
    Like findByPostIdAndUserId(String postId, String userId);
}
