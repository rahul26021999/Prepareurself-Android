package com.prepare.prepareurself.dashboard.data.model;

import java.util.List;

public class HomepageResponseModel {

    private boolean success;
    private int error_code;
    private String message;
    private List<HomepageData> data;

    public HomepageResponseModel(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HomepageData> getData() {
        return data;
    }

    public void setData(List<HomepageData> data) {
        this.data = data;
    }
}
