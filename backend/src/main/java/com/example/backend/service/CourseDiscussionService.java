package com.example.backend.service;

import com.example.backend.dto.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public interface CourseDiscussionService {
    void createDiscussion(CreateDiscussionDTO discussionDTO) throws SQLException;
    void createComment(CreateCommentDTO commentDTO);
    void deleteComment(DeleteCommentDTO deleteCommentDTO);
    boolean handleLikeAction(String userId, String coursename, String postId, boolean ifLike);
    void deletePost(String userId, String role, String coursename, String postId) throws SQLException;
    List<PostDTO> getPostsByCourseName(String coursename, String token) throws Exception;
    PostDetailDTO getPostDetail(String courseName, String postId, String userId) throws Exception;
    File downloadImage(String imageId) throws FileNotFoundException;
}
