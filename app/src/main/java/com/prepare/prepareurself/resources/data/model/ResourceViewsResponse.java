package com.prepare.prepareurself.resources.data.model;

public class ResourceViewsResponse {
    int error_code;
    String msg;

    public ResourceViewsResponse(){

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
}
