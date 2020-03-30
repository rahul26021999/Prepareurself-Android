package com.example.prepareurself.authentication.registration.presenter;

import android.content.Context;
import android.util.Log;

import com.example.prepareurself.Apiservice.ApiRepository;
import com.example.prepareurself.authentication.registration.model.RegisterResponseModel;
import com.example.prepareurself.authentication.registration.view.RegisterViewAction;
import com.example.prepareurself.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    Context context;
    RegisterViewAction viewAction;
    ApiRepository repository;

    public RegisterPresenter(Context context, RegisterViewAction viewAction, ApiRepository repository) {
        this.context = context;
        this.viewAction = viewAction;
        this.repository = repository;
    }

    public void onRegister(String firstname, String lastname, String username, String password, String email){
        viewAction.showLoader();
        repository.registerUser(firstname, lastname, username, password, email, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("register_url",response+"");
                if (response.body()!=null){
                    RegisterResponseModel responseModel = (RegisterResponseModel) response.body();
                    if (responseModel.error == 1){
                        viewAction.onFailure(Constants.INVALIDUSERDATA);
                    }else if (responseModel.error == 0){
                        viewAction.onRegistrationSuccess(responseModel);
                    }else{
                        viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                    }
                    viewAction.hideLoader();
                }else {
                    viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                    viewAction.hideLoader();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("register_url",t.getLocalizedMessage()+"");
                viewAction.hideLoader();
                viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
            }
        });
    }

}
