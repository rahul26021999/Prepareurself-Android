package com.prepare.prepareurself.favourites.data.model;

public class FavouritesResponseModel {

    private int error_code;
    private boolean success;
    private LikedItemsProjects projects;
    private LikedItemsResources resources;

    public FavouritesResponseModel(){

    }

    public LikedItemsResources getResources() {
        return resources;
    }

    public void setResources(LikedItemsResources resources) {
        this.resources = resources;
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

    public LikedItemsProjects getProjects() {
        return projects;
    }

    public void setProjects(LikedItemsProjects projects) {
        this.projects = projects;
    }
}
