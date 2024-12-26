package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.dto.*;
import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private DiscussionDAO discussionDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<LikedPostDTO> getLikedPosts(String userId) throws Exception {

        List<LikedPostDTO> likedPosts = new ArrayList<>();
        List<Like> likes = likeDAO.findLikesByUserId(userId);

        for (Like like : likes) {

            Discussion discussion = discussionDAO.findById(like.getPostId());
            Course course = courseDAO.getCourseById(discussion.getCourseId());
            User user = userDAO.getUserById(discussion.getUserId());

            if (discussion != null) {
                LikedPostDTO likedPost = new LikedPostDTO();
                likedPost.setTitle(discussion.getTitle());
                likedPost.setPostId(discussion.getPostId());
                likedPost.setCourseName(course.getCourseName());
                likedPost.setAuthor(user.getName());
                likedPost.setTime(discussion.getCreatedAt());

                likedPosts.add(likedPost);
            }
        }

        return likedPosts;
    }

    @Override
    public List<UserPostDTO> getUserPosts(String userId) throws SQLException {

        List<UserPostDTO> userPosts = new ArrayList<>();
        List<Discussion> discussions = discussionDAO.findPostsByUserId(userId);

        for (Discussion discussion : discussions) {

            UserPostDTO dto = new UserPostDTO();
            Course course = courseDAO.getCourseById(discussion.getCourseId());

            dto.setTitle(discussion.getTitle());
            dto.setPostId(discussion.getPostId());
            dto.setCourseName(course.getCourseName());
            dto.setTime(discussion.getCreatedAt());
            userPosts.add(dto);
        }
        return userPosts;
    }
}
