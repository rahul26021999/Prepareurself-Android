package com.example.prepareurself.authentication.registration.presenter;

import android.content.Context;

import com.example.prepareurself.Apiservice.ApiRepository;
import com.example.prepareurself.authentication.registration.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.registration.view.RegisterViewAction;
import com.example.prepareurself.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    Context context;
    ApiRepository apiRepository;
    RegisterViewAction registerViewAction;

    public LoginPresenter(Context context, ApiRepository apiRepository, RegisterViewAction registerViewAction) {
        this.context = context;
        this.apiRepository = apiRepository;
        this.registerViewAction = registerViewAction;
    }

    public void loginUser(String email, String password){
        registerViewAction.showLoader();
        apiRepository.loginUser(email, password, new Callback() {
            //callback is in  interface
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body()!=null){
                    AuthenticationResponseModel authenticationResponseModel = (AuthenticationResponseModel) response.body();
                    if (authenticationResponseModel.error_code==0){
                        registerViewAction.onRegistrationSuccess(authenticationResponseModel);

                    }
                    else{
                        registerViewAction.onFailure(authenticationResponseModel.msg);
                    }

                    registerViewAction.hideLoader();
                }
                else{
                    registerViewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                    registerViewAction.hideLoader();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                registerViewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                registerViewAction.hideLoader();
            }
        });
    }
}
