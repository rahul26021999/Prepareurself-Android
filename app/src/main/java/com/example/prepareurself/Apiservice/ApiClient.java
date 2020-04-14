package com.example.prepareurself.Apiservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASEURL =  "http://prepareurself.tk/api/"; // this is our base url, this will be same for all the endpoints

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build();
        if (retrofit==null) {
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

}
