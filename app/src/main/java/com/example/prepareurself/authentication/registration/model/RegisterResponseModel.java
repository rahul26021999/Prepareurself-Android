package com.example.prepareurself.authentication.registration.model;

public class RegisterResponseModel {
    public int error;
    public String msg;
    public User user_data;

    public RegisterResponseModel(int error, String msg, User user_data) {
        this.error = error;
        this.msg = msg;
        this.user_data = user_data;
    }
}
