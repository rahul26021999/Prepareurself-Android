package com.example.prepareurself.authentication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prepareurself.Home.HomeActivity;
import com.example.prepareurself.R;
import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.viewmodel.AuthViewModel;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.PrefManager;
import com.example.prepareurself.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String TAG="login button";
    private ProgressDialog progressDialog;
    private PrefManager prefManager;

    private AuthViewModel viewModel;

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

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

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

            viewModel.login(strEmail,strPassword);

            showLoader();

            viewModel.getLoginResultMutableLiveData().observe(getActivity(), new Observer<AuthenticationResponseModel>() {
                @Override
                public void onChanged(AuthenticationResponseModel authenticationResponseModel) {
                    if (authenticationResponseModel.getError_code() == 0){
                        prefManager.saveBoolean(Constants.ISLOGGEDIN, true);

                        prefManager.saveString(Constants.USERFIRSTNAME, authenticationResponseModel.getUser_data().getFirst_name());
                        prefManager.saveString(Constants.USERLASTNAME, authenticationResponseModel.getUser_data().getLast_name());
                        prefManager.saveString(Constants.USEREMAIL, authenticationResponseModel.getUser_data().getEmail());
                        prefManager.saveString(Constants.USERPASSWORD, authenticationResponseModel.getUser_data().getPassword());
                        prefManager.saveString(Constants.USER_USERNAME, authenticationResponseModel.getUser_data().getUsername());
                        prefManager.saveInteger(Constants.USERID, authenticationResponseModel.getUser_data().getId());

                        Utility.showToast(getActivity(),"Login done!");
                        Intent intent=new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Utility.showToast(getActivity(),authenticationResponseModel.getMsg());
                    }

                    hideLoader();
                }
            });
        }

}
    private boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }

    private void showLoader(){
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.setMessage("Logging In...");
            progressDialog.show();
        }
    }

    private void hideLoader(){
        if (progressDialog!=null &&  progressDialog.isShowing()){
            progressDialog.hide();
        }
    }
}
