package com.example.backend.dto;

public class LikeRequestDTO {
    private String coursename;
    private String postid;
    private boolean iflike;

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIflike() {
        return iflike;
    }

    public void setIflike(boolean iflike) {
        this.iflike = iflike;
    }
}
