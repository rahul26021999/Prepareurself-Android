package com.prepare.prepareurself.search;

import java.util.List;

public class SearchResponseModel {

    private int error_code;

    private List<SearchModel> data;

    public SearchResponseModel(){

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SearchModel> getData() {
        return data;
    }

    public void setData(List<SearchModel> data) {
        this.data = data;
    }
}
