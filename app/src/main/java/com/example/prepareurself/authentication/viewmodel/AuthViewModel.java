package com.example.prepareurself.authentication.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.data.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private MutableLiveData<Boolean> loaderVisibility = new MutableLiveData<>();
    private LiveData<AuthenticationResponseModel> loginResultMutableLiveData = new MutableLiveData<>();

    public LiveData<AuthenticationResponseModel> getLoginResultMutableLiveData() {
        return loginResultMutableLiveData;
    }

    public void login(String email, String password){
        loginResultMutableLiveData = AuthRepository.getInstance().login(email,password);
    }

}
