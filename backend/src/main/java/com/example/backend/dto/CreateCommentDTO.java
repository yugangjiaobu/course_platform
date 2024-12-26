package com.example.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class CreateCommentDTO {
    @JsonProperty("coursename")
    private String coursename;

    @JsonProperty("postid")
    private String postid;

    @JsonProperty("content")
    private String content;

    private String userId;

    public String getCourseName() {
        return coursename;
    }

    public void setCourseName(String courseName) {
        this.coursename = courseName;
    }

    public String getPostId() {
        return postid;
    }

    public void setPostId(String postId) {
        this.postid = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
