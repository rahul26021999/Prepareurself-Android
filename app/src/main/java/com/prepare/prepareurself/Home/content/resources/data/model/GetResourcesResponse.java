package com.prepare.prepareurself.Home.content.resources.data.model;

public class GetResourcesResponse {

    private int error_code;
    private ResourcesResponse resources;

    public GetResourcesResponse() {
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResourcesResponse getResources() {
        return resources;
    }

    public void setResources(ResourcesResponse resources) {
        this.resources = resources;
    }
}
