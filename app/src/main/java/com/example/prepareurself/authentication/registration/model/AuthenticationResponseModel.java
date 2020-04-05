package com.example.prepareurself.authentication.registration.model;

public class AuthenticationResponseModel {
    public int error_code;
    public String msg;
    public User user_data;

    public AuthenticationResponseModel(int error_code, String msg, User user_data) {
        this.error_code = error_code;
        this.msg = msg;
        this.user_data = user_data;
    }
}
