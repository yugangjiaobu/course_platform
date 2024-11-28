package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.dto.*;
import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseDiscussionServiceImpl implements CourseDiscussionService {

    @Autowired
    private DiscussionDAO discussionDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    private final String imageDirectory = "E:\\Idea_Project\\image\\";

    @Override
    public void createDiscussion(CreateDiscussionDTO discussionDTO) throws SQLException {
        String postId = UUID.randomUUID().toString();
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        if (discussionDTO.getImages() != null && !discussionDTO.getImages().isEmpty()) {
            for (MultipartFile img : discussionDTO.getImages()) {
                String imageId = UUID.randomUUID().toString();
                String imagePath = imageDirectory + imageId + "_" + img.getOriginalFilename();

                try {
                    File file = new File(imagePath);
                    img.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image file", e);
                }

                Discussion discussion = new Discussion();
                Course course = courseDAO.getCourseByName(discussionDTO.getCourseName());
                discussion.setPostId(postId);
                discussion.setCourseId(course.getCourseId());
                discussion.setUserId(discussionDTO.getUserId());
                discussion.setContent(discussionDTO.getContent());
                discussion.setCreatedAt(createdAt);
                discussion.setImageId(imageId);
                discussion.setImagePath(imagePath);
                discussion.setTitle(discussionDTO.getTitle());

                discussionDAO.save(discussion);
            }
        } else {
            // 没有图片时，直接创建讨论，不设置图片路径
            String imageId = discussionDTO.getImages() != null && !discussionDTO.getImages().isEmpty()
                    ? UUID.randomUUID().toString()
                    : "no_image_" + UUID.randomUUID().toString();
            String imagePath = null;
            Discussion discussion = new Discussion();
            Course course = courseDAO.getCourseByName(discussionDTO.getCourseName());
            discussion.setPostId(postId);
            discussion.setCourseId(course.getCourseId());
            discussion.setUserId(discussionDTO.getUserId());
            discussion.setContent(discussionDTO.getContent());
            discussion.setCreatedAt(createdAt);
            discussion.setImageId(imageId);
            discussion.setImagePath(imagePath);
            discussion.setTitle(discussionDTO.getTitle());

            discussionDAO.save(discussion);
        }

    }

    @Override
    public void createComment(CreateCommentDTO commentDTO) {
        String commentId = UUID.randomUUID().toString();
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        //System.out.println(commentDTO.getPostId());
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setPostId(commentDTO.getPostId());
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(createdAt);
        //System.out.println(comment.getPostId());

        commentDAO.save(comment);
    }

    @Override
    public void deleteComment(DeleteCommentDTO deleteCommentDTO) throws SQLException {
        Comment comment = commentDAO.findById(deleteCommentDTO.getCommentId());
        Course course = courseDAO.getCourseByName(deleteCommentDTO.getCourseName());

        if (comment != null && ((comment.getUserId().equals(deleteCommentDTO.getUserId())) ||
                (deleteCommentDTO.getUserId().equals(course.getTeacherId())))) {
            commentDAO.delete(comment);
        } else {
            throw new RuntimeException("Comment not found or user not authorized to delete");
        }
    }

    @Override
    public boolean handleLikeAction(String userId, String coursename, String postId, boolean ifLike) {
        Like existingLike = likeDAO.findByPostIdAndUserId(postId, userId);

        try {
            if (ifLike) {
                if (existingLike == null) {
                    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                    Like newLike = new Like();
                    String likeid = UUID.randomUUID().toString();
                    newLike.setLikeId(likeid);
                    newLike.setPostId(postId);
                    newLike.setUserId(userId);
                    newLike.setCreatedAt(createdAt);
                    likeDAO.save(newLike);
                }
            } else {
                if (existingLike != null) {
                    likeDAO.delete(existingLike);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deletePost(String userId, String role, String coursename, String postId) throws SQLException {
        // 查询帖子信息
        List<Discussion> discussions = discussionDAO.findAll().stream()
                .filter(discussion -> discussion.getPostId().equals(postId))
                .collect(Collectors.toList());
        if (discussions.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        // 验证用户权限
        Course course = courseDAO.getCourseByName(coursename);
        Discussion firstDiscussion = discussions.get(0); // 多条记录中权限一致，取第一条检查
        boolean isTeacher = "teacher".equals(role) && firstDiscussion.getCourseId().equals(course.getCourseId());
        boolean isCreator = firstDiscussion.getUserId().equals(userId);
        if (!isTeacher && !isCreator) {
            throw new IllegalArgumentException("User not authorized to delete this post");
        }

        // 删除评论
        List<Comment> comments = commentDAO.findAll().stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .collect(Collectors.toList());
        for (Comment comment : comments) {
            commentDAO.delete(comment);
        }

        // 删除点赞
        List<Like> likes = likeDAO.findAll().stream()
                .filter(like -> like.getPostId().equals(postId))
                .collect(Collectors.toList());
        for (Like like : likes) {
            likeDAO.delete(like);
        }

        // 删除帖子关联的图片文件
        for (Discussion discussion : discussions) {
            if (discussion.getImagePath() != null) {
                File imageFile = new File(discussion.getImagePath());
                if (imageFile.exists()) {
                    boolean deleted = imageFile.delete();
                    if (!deleted) {
                        throw new RuntimeException("Failed to delete image file: " + discussion.getImagePath());
                    }
                }
            }
            // 删除帖子记录
            discussionDAO.delete(discussion);
        }
    }

    @Override
    public List<PostDTO> getPostsByCourseName(String coursename, String token) throws Exception {
        Course course = courseDAO.getCourseByName(coursename);
        if (course == null) {
            throw new Exception("Course not found");
        }

        List<Discussion> allDiscussions = discussionDAO.findAllByCourseId(course.getCourseId());

        Map<String, List<Discussion>> groupedDiscussions = allDiscussions.stream()
                .collect(Collectors.groupingBy(Discussion::getPostId));

        List<PostDTO> postDTOList = new ArrayList<>();
        for (Map.Entry<String, List<Discussion>> entry : groupedDiscussions.entrySet()) {
            String postId = entry.getKey();
            List<Discussion> discussions = entry.getValue();

            Discussion firstDiscussion = discussions.get(0);
            User author = userDAO.getUserById(firstDiscussion.getUserId());
            if (author == null) {
                throw new Exception("Author not found for post: " + postId);
            }

            PostDTO postDTO = new PostDTO(
                    firstDiscussion.getTitle(),
                    postId,
                    author.getName(),
                    firstDiscussion.getCreatedAt()
            );
            postDTOList.add(postDTO);
        }

        return postDTOList;
    }

    @Override
    public PostDetailDTO getPostDetail(String courseName, String postId, String userId) throws Exception {
        // 1. 获取帖子信息
        Discussion discussion = discussionDAO.findById(postId);
        if (discussion == null) {
            return null;
        }

        // 2. 获取作者信息
        User author = userDAO.getUserById(discussion.getUserId());

        Course course = courseDAO.getCourseByName(courseName);

        // 3. 获取评论信息
        List<Comment> comments = commentDAO.findByPostId(postId);
        List<PostDetailDTO.CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            PostDetailDTO.CommentDTO commentDTO = new PostDetailDTO.CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setName(userDAO.getUserById(comment.getUserId()).getName());
            commentDTO.setIfYours(userId.equals(course.getTeacherId()) || (userId.equals(comment.getUserId())));
            commentDTOs.add(commentDTO);
        }

        // 4. 获取点赞信息
        int likesCount = likeDAO.countLikesByPostId(postId);
        boolean ifLike = likeDAO.findByPostIdAndUserId(postId, userId) != null;

        // 5. 获取图片信息
        List<String> imgs = discussionDAO.findImagesByPostId(postId).stream()
                .map(imageId -> "/api/downloadimage/" + imageId) // 使用 image_id 构造下载 URL
                .collect(Collectors.toList());

        //System.out.println(imgs);

        // 6. 创建返回的DTO对象
        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setTitle(discussion.getTitle());
        postDetailDTO.setContent(discussion.getContent());
        postDetailDTO.setAuthor(author.getName());
        postDetailDTO.setTime(discussion.getCreatedAt().toString());
        postDetailDTO.setImgs(imgs);
        postDetailDTO.setLikes(likesCount);
        postDetailDTO.setIfLike(ifLike);
        postDetailDTO.setCommits(commentDTOs);

        //System.out.println(postDetailDTO.getImgs());

        return postDetailDTO;
    }

    @Override
    public File downloadImage(String imageId) throws FileNotFoundException {
        // 根据图片 ID 查询对应的帖子记录，假设帖子表中存储了图片路径
        Discussion discussion = discussionDAO.findByImageId(imageId);
        if (discussion == null) {
            throw new FileNotFoundException("帖子未找到，无法获取图片");
        }

        // 根据图片 ID 获取图片的实际路径
        String imagePath = discussion.getImagePath();  // 假设 imagePath 是存储在数据库中的图片路径
        if (imagePath == null || imagePath.isEmpty()) {
            throw new FileNotFoundException("图片路径为空");
        }

        //System.out.println(imagePath);

        // 创建 File 对象并返回
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new FileNotFoundException("图片文件不存在");
        }

        return imageFile;
    }

}
