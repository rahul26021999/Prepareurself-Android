package com.example.prepareurself.authentication.registration.presenter;

import android.content.Context;
import android.util.Log;

import com.example.prepareurself.Apiservice.ApiRepository;
import com.example.prepareurself.authentication.registration.model.CheckUsernameResponse;
import com.example.prepareurself.authentication.registration.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.registration.view.RegisterViewAction;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.Utility;

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
        repository.registerUser(firstname, lastname, username, password, email, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("register_url",response+"");
                if (response.body()!=null){
                    AuthenticationResponseModel responseModel = (AuthenticationResponseModel) response.body();
                    if (responseModel.error_code == 1){
                        viewAction.onFailure(Constants.INVALIDUSERDATA);
                    }else if (responseModel.error_code == 0){
                        viewAction.onRegistrationSuccess(responseModel);
                    }else{
                        viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                        viewAction.hideLoader();
                    }
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

    public void checkUsernameAndRegister(final String firstname, final String lastname, final String userName, final String password, final String email){
        viewAction.showLoader();
        repository.checkUsername(userName, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body()!=null){
                    CheckUsernameResponse checkUsernameResponse = (CheckUsernameResponse) response.body();
//                    viewAction.onCheckUserName(checkUsernameResponse);
                    if (checkUsernameResponse.error == 2){
                        Utility.showToast(context,checkUsernameResponse.msg);
                        viewAction.hideLoader();
                    }else if (checkUsernameResponse.error == 0){
                        onRegister(firstname, lastname,userName,password,email);
                    }
                    viewAction.hideLoader();
                }else{
                    viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
                    viewAction.hideLoader();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                viewAction.hideLoader();
                viewAction.onFailure(Constants.SOMETHINGWENTWRONG);
            }
        });

    }

}
