package com.prepare.prepareurself.authentication.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiInterface apiInterface;
    private UserDBRepository userDBRepository;

    public AuthRepository(Application application){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        userDBRepository = new UserDBRepository(application);
    }

    public LiveData<AuthenticationResponseModel> login(String email, String password){

        final MutableLiveData<AuthenticationResponseModel> data = new MutableLiveData<>();

        apiInterface.loginUser(email,password).enqueue(new Callback<AuthenticationResponseModel>() {
            @Override
            public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                AuthenticationResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.isSuccess()){
                        data.setValue(responseModel);
                        userDBRepository.clearUser();
                        userDBRepository.insertUser(responseModel.getUser());
                    }else{
                        data.setValue(responseModel);
                    }
                }else{
                    if (response.errorBody()!=null){
                        try {
                            AuthenticationResponseModel model = new AuthenticationResponseModel();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            model.setMessage(jsonObject.getString("message"));
                            data.setValue(model);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            data.setValue(null);
                        }
                    }else{
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<RegisterResponseModel> register(String firstName, String lastName, String email, String password, String androidToken){

        final MutableLiveData<RegisterResponseModel> data = new MutableLiveData<>();

        apiInterface.registerUser(firstName, lastName, password, email, androidToken).enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                RegisterResponseModel responseModel = response.body();
                if (responseModel != null) {
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
