package com.example.prepareurself.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String TAG="login button";
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
            Log.d(TAG,"login validation is working fine");
        }
}
    private boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }
}
