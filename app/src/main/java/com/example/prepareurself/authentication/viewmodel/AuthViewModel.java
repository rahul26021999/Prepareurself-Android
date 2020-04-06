package com.example.prepareurself.authentication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<AuthenticationResponseModel> authenticationResponseModelMutableLiveData = new MutableLiveData<>();
    AuthRepository repository;

    public AuthViewModel(Application application){
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<AuthenticationResponseModel> getAuthenticationResponseModelMutableLiveData() {
        return authenticationResponseModelMutableLiveData;
    }

    public void login(String email, String password){
        authenticationResponseModelMutableLiveData = repository.login(email,password);
    }

    public void register(String firstname, String lastname, String email, String password){
        authenticationResponseModelMutableLiveData = repository.register(firstname, lastname, email, password);
    }

}
