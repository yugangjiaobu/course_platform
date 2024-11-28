package com.example.backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class DeleteDiscussionDTO {
    @JsonProperty("coursename")
    private String coursename;

    @JsonProperty("postid")
    private String postid;

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
}
