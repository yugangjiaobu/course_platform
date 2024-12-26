package com.example.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class DeleteCommentDTO {
    @JsonProperty("coursename")
    private String coursename;

    @JsonProperty("postid")
    private String postid;

    @JsonProperty("commentid")
    private String commentid;

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

    public String getCommentId() {
        return commentid;
    }

    public void setCommentId(String commentId) {
        this.commentid = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
