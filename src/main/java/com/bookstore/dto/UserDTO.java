package com.bookstore.dto;

public class UserDTO {

    private Long id;
    private String username;
    private String token;
    private String refreshToken;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String username, String token, String refreshToken) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

