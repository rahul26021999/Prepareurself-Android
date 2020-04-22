package com.prepare.prepareurself.authentication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;
import com.prepare.prepareurself.authentication.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<RegisterResponseModel> registerResponseModelLiveData = new MutableLiveData<>();
    private LiveData<AuthenticationResponseModel> authenticationResponseModelMutableLiveData = new MutableLiveData<>();
    AuthRepository repository;

    public AuthViewModel(Application application){
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<AuthenticationResponseModel> getAuthenticationResponseModelMutableLiveData() {
        return authenticationResponseModelMutableLiveData;
    }

    public LiveData<RegisterResponseModel> getRegisterResponseModelLiveData(){
        return registerResponseModelLiveData;
    }

    public void login(String email, String password){
        authenticationResponseModelMutableLiveData = repository.login(email,password);
    }

    public void register(String firstname, String lastname, String email, String password, String androidToken){
        registerResponseModelLiveData = repository.register(firstname, lastname, email, password, androidToken);
    }

}
