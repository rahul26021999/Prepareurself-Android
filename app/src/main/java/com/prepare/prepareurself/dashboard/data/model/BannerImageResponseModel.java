package com.prepare.prepareurself.dashboard.data.model;

import java.util.List;

public class BannerImageResponseModel {

    private int error_code;
    List<BannerModel> banner;


    public BannerImageResponseModel(){

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<BannerModel> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerModel> banner) {
        this.banner = banner;
    }
}
