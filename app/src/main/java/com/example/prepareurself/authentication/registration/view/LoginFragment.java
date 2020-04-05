package com.example.prepareurself.authentication.registration.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prepareurself.Apiservice.ApiRepository;
import com.example.prepareurself.Home.HomeActivity;
import com.example.prepareurself.R;
import com.example.prepareurself.authentication.registration.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.registration.presenter.LoginPresenter;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.PrefManager;
import com.example.prepareurself.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, RegisterViewAction {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String TAG="login button";
    private ProgressDialog progressDialog;
    private PrefManager prefManager;
    private LoginPresenter loginPresenter;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);

        etEmail =v.findViewById(R.id.et_email);
        etPassword =v.findViewById(R.id.et_password);
        btnLogin =v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        progressDialog=new ProgressDialog(this.getActivity());
        prefManager=new PrefManager(this.getActivity());
        loginPresenter=new LoginPresenter(this.getActivity(),new ApiRepository(),this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_login){
            String strEmail= etEmail.getText().toString().trim();
            String strPassword= etPassword.getText().toString().trim();
            if(TextUtils.isEmpty(strEmail)){
                etEmail.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(!isValidEmail(strEmail)){
                etEmail.setError(Constants.INVALIDEMAIL);
                return;
            }
            if(TextUtils.isEmpty(strPassword)){
                etPassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            loginPresenter.loginUser(strEmail,strPassword);
        }

}
    private boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }

    @Override
    public void onRegistrationSuccess(AuthenticationResponseModel authenticationResponseModel) {
        prefManager.saveBoolean(Constants.ISLOGGEDIN, true);
        // save the user data to prefmanager also

        prefManager.saveString(Constants.USERFIRSTNAME, authenticationResponseModel.user_data.first_name);
        prefManager.saveString(Constants.USERLASTNAME, authenticationResponseModel.user_data.last_name);
        prefManager.saveString(Constants.USEREMAIL, authenticationResponseModel.user_data.email);
        prefManager.saveString(Constants.USERPASSWORD, authenticationResponseModel.user_data.password);
        prefManager.saveString(Constants.USER_USERNAME, authenticationResponseModel.user_data.username);
        prefManager.saveInteger(Constants.USERID, authenticationResponseModel.user_data.id);

        Utility.showToast(this.getActivity(),"Login done!");
        Intent intent=new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onFailure(String errorMsg) {
        Utility.showToast(this.getActivity(),errorMsg);
    }

    @Override
    public void showLoader() {
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.setMessage("Logging In...");
            progressDialog.show();
        }
    }

    @Override
    public void hideLoader() {
        if (progressDialog!=null &&  progressDialog.isShowing()){
            progressDialog.hide();
        }
    }
}
