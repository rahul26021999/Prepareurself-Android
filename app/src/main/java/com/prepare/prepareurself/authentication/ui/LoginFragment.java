package com.prepare.prepareurself.authentication.ui;

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

import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.viewmodel.AuthViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;


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

            viewModel.getAuthenticationResponseModelMutableLiveData().observe(getActivity(), new Observer<AuthenticationResponseModel>() {
                @Override
                public void onChanged(AuthenticationResponseModel authenticationResponseModel) {
                    if (authenticationResponseModel!=null){
                        if (authenticationResponseModel.isSuccess()){
                            prefManager.saveBoolean(Constants.ISLOGGEDIN, true);
                            prefManager.saveString(Constants.JWTTOKEN,authenticationResponseModel.getToken());

                            Utility.showToast(getActivity(),"Login done!");
                            Intent intent=new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            Utility.showToast(getActivity(),authenticationResponseModel.getMsg());
                        }
                    }else{
                        Utility.showToast(getActivity(),Constants.SOMETHINGWENTWRONG);
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