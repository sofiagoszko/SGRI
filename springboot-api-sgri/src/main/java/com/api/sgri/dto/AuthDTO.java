package com.api.sgri.dto;

public class AuthDTO {

    private String userName;
    private String password;

    public AuthDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
