package com.example.prepareurself.authentication.registration.view;

import com.example.prepareurself.authentication.registration.model.CheckUsernameResponse;
import com.example.prepareurself.authentication.registration.model.RegisterResponseModel;

public interface RegisterViewAction {

    void onRegistrationSuccess(RegisterResponseModel registerResponseModel);
    void onFailure(String errorMsg);
    void showLoader();
    void hideLoader();
}
