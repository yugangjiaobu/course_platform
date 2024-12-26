package com.example.backend.dto;

import java.sql.Timestamp;

public class PostDTO {
    private String title;
    private String postId;
    private String author;
    private Timestamp time;

    public PostDTO(String title, String postId, String author, Timestamp time) {
        this.title = title;
        this.postId = postId;
        this.author = author;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

}
