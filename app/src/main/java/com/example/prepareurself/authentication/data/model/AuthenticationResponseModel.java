package com.example.prepareurself.authentication.data.model;

public class AuthenticationResponseModel {
    private int error_code;
    private String msg;
    private UserModel user_data;

    public AuthenticationResponseModel() {
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserModel getUser_data() {
        return user_data;
    }

    public void setUser_data(UserModel user_data) {
        this.user_data = user_data;
    }
}
