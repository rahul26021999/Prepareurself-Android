package com.prepare.prepareurself.authentication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.data.model.ForgotPasswordResponseModel;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.authentication.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<RegisterResponseModel> registerResponseModelLiveData = new MutableLiveData<>();
    private LiveData<AuthenticationResponseModel> authenticationResponseModelMutableLiveData = new MutableLiveData<>();
    AuthRepository repository;
    private UserDBRepository userDBRepository;

    public AuthViewModel(Application application){
        super(application);
        repository = new AuthRepository(application);
        userDBRepository = new UserDBRepository(application);
    }

    public LiveData<AuthenticationResponseModel> getAuthenticationResponseModelMutableLiveData() {
        return authenticationResponseModelMutableLiveData;
    }

    public LiveData<RegisterResponseModel> getRegisterResponseModelLiveData(){
        return registerResponseModelLiveData;
    }

    public void login(String email, String password, String androidToken){
        authenticationResponseModelMutableLiveData = repository.login(email,password, androidToken);
    }

    public void socialRegister(String firstName, String lastName, String androidToken, String googleId, String email, String profileImage){
        authenticationResponseModelMutableLiveData = repository.socialRegister(firstName, lastName, androidToken, googleId, email, profileImage);
    }

    public void register(String firstname, String lastname, String email, String password, String androidToken){
        registerResponseModelLiveData = repository.register(firstname, lastname, email, password, androidToken);
    }

    public LiveData<ForgotPasswordResponseModel> forgotPassword(String email){
        return repository.forgotPassword(email);
    }

    public LiveData<UserModel> userModelLiveData(){
        return userDBRepository.getUserInfo();
    }

    public void saveUseData(UserModel userModel){
        userDBRepository.insertUser(userModel);
    }


}
