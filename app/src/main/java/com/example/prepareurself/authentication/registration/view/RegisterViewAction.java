package com.example.prepareurself.authentication.registration.view;

import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;

public interface RegisterViewAction {

    void onRegistrationSuccess(AuthenticationResponseModel authenticationResponseModel);
    void onFailure(String errorMsg);
    void showLoader();
    void hideLoader();
}
