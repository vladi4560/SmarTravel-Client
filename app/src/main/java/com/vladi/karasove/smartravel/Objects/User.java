package com.vladi.karasove.smartravel.Objects;

public class User {
    private String avatar;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String domain;

    public User(){
        this.role= "PLAYER";
        this.domain="";
    }

    public User(String avatar, String firstName, String lastName, String email, String yearBirth, String gender) {
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role= "PLAYER";
        this.domain="";
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDomain() {
        return domain;
    }

    public User setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}