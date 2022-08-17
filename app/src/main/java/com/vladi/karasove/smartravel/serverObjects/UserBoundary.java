package com.vladi.karasove.smartravel.serverObjects;

public class UserBoundary {


    private DomainWithEmail userId;
    private String role;
    private String username;
    private String avatar;
    private String email;

    public UserBoundary() {
    }

    public UserBoundary(DomainWithEmail userId) {
        this.userId = userId;
    }

    public DomainWithEmail getUserId() {
        return userId;
    }

    public void setUserId(DomainWithEmail userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getEmail() {
        return email;
    }

    public UserBoundary setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return "UserBoundary{" +
                "userId=" + userId +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}