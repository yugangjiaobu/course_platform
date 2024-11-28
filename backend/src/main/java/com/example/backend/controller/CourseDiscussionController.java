package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.CourseDiscussionService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class CourseDiscussionController {

    @Autowired
    private CourseDiscussionService courseDiscussionService;

    @PostMapping("/post")
    public ResponseEntity<String> createPost(
            @RequestPart("coursename") String coursename,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "imgs", required = false) List<MultipartFile> imgs,
            @RequestHeader("Authorization") String token) throws SQLException {

        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);

        CreateDiscussionDTO discussionDTO = new CreateDiscussionDTO();
        discussionDTO.setCourseName(coursename);
        discussionDTO.setTitle(title);
        discussionDTO.setContent(content);
        discussionDTO.setUserId(userId);
        discussionDTO.setImages(imgs);

        courseDiscussionService.createDiscussion(discussionDTO);

        return ResponseEntity.ok("Post created successfully!");
    }

    @PostMapping("/comment")
    public ResponseEntity<String> createComment(
            @RequestBody CreateCommentDTO commentDTO,
            @RequestHeader("Authorization") String token) {
        //System.out.println(commentDTO.getPostId());
        //System.out.println(commentDTO.getContent());
        //System.out.println(commentDTO.getCourseName());

        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);

        commentDTO.setUserId(userId);

        courseDiscussionService.createComment(commentDTO);

        return ResponseEntity.ok("Comment added successfully!");
    }

    @PostMapping("/deletecomment")
    public ResponseEntity<String> deleteComment(
            @RequestBody DeleteCommentDTO deleteCommentDTO,
            @RequestHeader("Authorization") String token) {

        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);
        deleteCommentDTO.setUserId(userId);

        //System.out.println(deleteCommentDTO.getCommentId());
        //System.out.println(deleteCommentDTO.getCourseName());
        //System.out.println(deleteCommentDTO.getPostId());
        courseDiscussionService.deleteComment(deleteCommentDTO);

        return ResponseEntity.ok("Comment deleted successfully!");
    }

    @PostMapping("/like")
    public String handleLikeAction(@RequestBody LikeRequestDTO likeRequestDTO, @RequestHeader("Authorization") String token) {
        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);
        //System.out.println(likeRequestDTO.isIflike());
        boolean success = courseDiscussionService.handleLikeAction(
                userId,
                likeRequestDTO.getCoursename(),
                likeRequestDTO.getPostid(),
                likeRequestDTO.isIflike()
        );



        return success ? "Success" : "Failed";
    }

    @PostMapping("/deletepost")
    public ResponseEntity<String> deletePost(@RequestBody DeleteDiscussionDTO requestDTO,@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = extractJwtToken(token);
            String userId = JWTUtil.extractUserID(jwtToken);
            String role = JWTUtil.extractRole(jwtToken);
            //System.out.println(requestDTO.getCoursename());
            //System.out.println(requestDTO.getPostid());
            courseDiscussionService.deletePost(userId, role, requestDTO.getCoursename(), requestDTO.getPostid());
            return ResponseEntity.ok("Post deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the post");
        }
    }

    @GetMapping("/getpostlist")
    public ResponseEntity<List<PostDTO>> getPostList(
            @RequestParam String coursename,
            @RequestHeader("Authorization") String token) {
        try {
            List<PostDTO> posts = courseDiscussionService.getPostsByCourseName(coursename, token);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getpost")
    public PostDetailDTO getPostDetail(
            @RequestParam String coursename,
            @RequestParam String postid,
            @RequestHeader("Authorization") String token) throws Exception {
        //System.out.println(coursename);
        //System.out.println(postid);
        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);

        PostDetailDTO postDetailDTO = courseDiscussionService.getPostDetail(coursename, postid, userId);
        //System.out.println(postDetailDTO.getImgs());

        return postDetailDTO;
    }

    @GetMapping("/downloadimage/{imageId}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable("imageId") String imageId) throws IOException {
        try {
            // 获取图片文件
            //System.out.println(imageId);
            File file = courseDiscussionService.downloadImage(imageId);

            // 对文件名进行 URL 编码，确保中文字符可以正确处理
            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            // 设置响应头，使用 UTF-8 编码格式
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            // 创建文件输入流
            FileInputStream inputStream = new FileInputStream(file);

            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // 如果无法识别类型，则默认为二进制流
            }

            // 返回文件内容，使用 InputStreamResource 实现流式传输
            InputStreamResource resource = new InputStreamResource(inputStream);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);

        } catch (IOException e) {
            // 文件未找到或读取错误的处理
            return ResponseEntity.status(404).body(null);
        }
    }

    // 提取 JWT token
    private String extractJwtToken(String authHeader) {
        String[] parts = authHeader.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            return null;
        }
        return parts[1];
    }
}
