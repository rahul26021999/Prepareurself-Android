package com.example.prepareurself.authentication.registration.model;

public class CheckUsernameResponse {

    public String msg;
    public int error;

    public CheckUsernameResponse(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }
}
