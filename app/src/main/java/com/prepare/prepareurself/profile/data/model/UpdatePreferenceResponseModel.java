package com.prepare.prepareurself.profile.data.model;

import com.prepare.prepareurself.authentication.data.model.UserModel;

public class UpdatePreferenceResponseModel {

    private int error_code;
    private UserModel user_data;
    private String msg;

    public UpdatePreferenceResponseModel(){

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public UserModel getUser_data() {
        return user_data;
    }

    public void setUser_data(UserModel user_data) {
        this.user_data = user_data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
