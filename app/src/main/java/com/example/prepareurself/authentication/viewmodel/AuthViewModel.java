package com.example.prepareurself.authentication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.data.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private LiveData<AuthenticationResponseModel> authenticationResponseModelMutableLiveData = new MutableLiveData<>();

    public LiveData<AuthenticationResponseModel> getAuthenticationResponseModelMutableLiveData() {
        return authenticationResponseModelMutableLiveData;
    }

    public void login(String email, String password){
        authenticationResponseModelMutableLiveData = AuthRepository.getInstance().login(email,password);
    }

    public void register(String firstname, String lastname, String email, String password){
        authenticationResponseModelMutableLiveData = AuthRepository.getInstance().register(firstname, lastname, email, password);
    }

}
