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
import android.widget.Toast;

import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText etFullname, etEmail, etPassword, etRetypePassword;
    private Button btnRegister;
    private String TAG="onclick of register button";
    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_register, container, false);

        etFullname =v.findViewById(R.id.et_name);
        etEmail =v.findViewById(R.id.et_email);
        etPassword =v.findViewById(R.id.et_password);
        etRetypePassword =v.findViewById(R.id.et_repassword);
        btnRegister =v.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_register ){

            String str_password= etPassword.getText().toString().trim();
            String str_retype_password= etRetypePassword.getText().toString().trim();
            String str_email= etEmail.getText().toString().trim();

            if(TextUtils.isEmpty(etFullname.getText().toString().trim())){
                //Toast.makeText(this.getActivity(),"you did not enter value",Toast.LENGTH_SHORT).show();
                etFullname.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(TextUtils.isEmpty(etEmail.getText().toString().trim())){
                etEmail.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(!Utility.isValidEmail(str_email)){
                etEmail.setError(Constants.INVALIDEMAIL);
                return;
            }
            if(TextUtils.isEmpty(etPassword.getText().toString())){
                etPassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(TextUtils.isEmpty(etRetypePassword.getText().toString())){
                etRetypePassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if (!TextUtils.isEmpty(etPassword.getText().toString().trim()) && !TextUtils.isEmpty(str_retype_password.trim())){
                if(etPassword.getText().toString().length()<8) {
                    etPassword.setError(Constants.PASSWORDMUSTBEATLEAST8CHARACTERS);
                    return;
                }
                if (!str_password.equals(str_retype_password)){ ///use .equals functon rather than comparing
                    etRetypePassword.setError(Constants.PASSWORDNOTMATCHED);
                    return;
                }
            }


            Log.d(TAG,"heloo its working");
            //ether u can make evry if with return so next code doesnt work f any if fails to pass or you can put all is statemnts (validating code) in 1 function and then put else for work that has to be done on click of button

        }

    }
}
