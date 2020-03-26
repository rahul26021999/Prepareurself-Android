package com.example.prepareurself.Apiservice;

public class ApiRepository {

    ApiInterface apiInterface=ApiClient.getApiClient().create(ApiInterface.class);

    public ApiRepository() {
    }

}
