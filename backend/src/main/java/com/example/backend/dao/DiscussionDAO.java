package com.example.backend.dao;

import com.example.backend.entity.Discussion;

import java.util.List;

public interface DiscussionDAO {
    void save(Discussion discussion);
    Discussion findById(String postId);
    void update(Discussion discussion);
    void delete(Discussion discussion);
    List<Discussion> findAll();
}