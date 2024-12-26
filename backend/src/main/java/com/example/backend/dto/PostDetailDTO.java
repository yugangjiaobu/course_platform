package com.example.backend.dto;

import java.util.List;

public class PostDetailDTO {
    private String title;
    private String content;
    private String author;
    private String time;
    private List<String> imgs;
    private int likes;
    private boolean ifLike;
    private List<CommentDTO> commits;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isIfLike() {
        return ifLike;
    }

    public void setIfLike(boolean ifLike) {
        this.ifLike = ifLike;
    }

    public List<CommentDTO> getCommits() {
        return commits;
    }

    public void setCommits(List<CommentDTO> commits) {
        this.commits = commits;
    }

    public static class CommentDTO {
        private String name;
        private String content;
        private String commentId;
        private boolean ifYours;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public boolean isIfYours() {
            return ifYours;
        }

        public void setIfYours(boolean ifYours) {
            this.ifYours = ifYours;
        }
    }
}
