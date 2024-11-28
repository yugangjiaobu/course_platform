package com.example.backend.dao;

import com.example.backend.entity.Comment;

import java.util.List;

public interface CommentDAO {
    void save(Comment comment);
    Comment findById(String commentId);
    void update(Comment comment);
    void delete(Comment comment);
    List<Comment> findAll();
    List<Comment> findByPostId(String postId);
}
