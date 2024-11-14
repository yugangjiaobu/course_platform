package com.example.backend.dto;

public class CourseResourceDTO {
    private String name;
    private String id;
    private String url;

    // Constructor
    public CourseResourceDTO(String name, String id, String url) {
        this.name = name;
        this.id = id;
        this.url = url;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
