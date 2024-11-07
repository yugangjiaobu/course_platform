package com.example.backend.dto;

public class UserInfoDTO {
    private String id;
    private String name;
    private String email;
    private String tel;
    private String role;

    // 构造函数
    public UserInfoDTO(String id, String name, String email, String tel, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.role = role;
    }

    // Getters 和 Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
