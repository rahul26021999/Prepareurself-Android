package com.prepare.prepareurself.Home.content.profile.data.repository;

import android.util.Log;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private ApiInterface apiInterface;

    public ProfileRepository(){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
    }

    public void updateUser(String token, String firstName, String lastName, String dob, String phoneNumber){
        apiInterface.updateUser(token, firstName, lastName,dob , phoneNumber).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!=null){
                    Log.d("update_user_response", response.body()+"");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("update_user_response", "failure" +t.getLocalizedMessage()+"");
            }
        });
    }

    public void updatePreferences(String token, List<Integer> integers) {
        Log.d("update_prefernce_respon",apiInterface.updatePreference(token,integers).request().url().toString());
        apiInterface.updatePreference(token,integers).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!=null){
                    Log.d("update_prefernce_respon",response.body()+"");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("update_prefernce_respon",t.getLocalizedMessage()+"");
            }
        });
    }
}
