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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText email, password;
    Button login;
    String TAG="login button";
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);
        email=v.findViewById(R.id.et_email);
        password=v.findViewById(R.id.et_password);
        login=v.findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        return v;
        }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_login){
            String str_email=email.getText().toString().trim();
            String str_password=password.getText().toString().trim();
            if(TextUtils.isEmpty(str_email)){
                email.setError("field is empty");
                return;
            }
            if(!isValidEmail(str_email)){
                email.setError("email valid email");
            }
            if(TextUtils.isEmpty(password.getText().toString())){
                password.setError("field is empty");
                return;
            }
            Log.d(TAG,"login validation is working fine");
        }
}
    public static boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }
}
