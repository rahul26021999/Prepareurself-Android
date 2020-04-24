package com.prepare.prepareurself.resources.data.model;

public class VideoShareResponseModel {

    private boolean success;
    private int error_code;
    private ResourceModel resource;

    public VideoShareResponseModel(){

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

    public ResourceModel getResource() {
        return resource;
    }

    public void setResource(ResourceModel resource) {
        this.resource = resource;
    }
}
