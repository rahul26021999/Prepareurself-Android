package com.prepare.prepareurself.favourites.data.model;

public class FavouritesResponseModel {

    private int error_code;
    private LikedItems likedItems;

    public FavouritesResponseModel(){

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public LikedItems getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(LikedItems likedItems) {
        this.likedItems = likedItems;
    }
}
