package com.example.prepareurself.Fragments;

import android.content.Context;
import android.media.MediaCodec;
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

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    EditText fullname, email, password, retype_password;
    Button btn_register;
    String TAG="onclick of register button";
    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_register, container, false);
        fullname =v.findViewById(R.id.et_name);
        email=v.findViewById(R.id.et_email);
        password=v.findViewById(R.id.et_password);
        retype_password=v.findViewById(R.id.et_repassword);
        btn_register=v.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;



    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_register ){

            String str_password=password.getText().toString().trim();
            String str_retype_password=retype_password.getText().toString().trim();
            String str_email=email.getText().toString().trim();

            if(TextUtils.isEmpty(fullname.getText().toString().trim())){
                //Toast.makeText(this.getActivity(),"you did not enter value",Toast.LENGTH_SHORT).show();
                fullname.setError("field is empty");
                return;
            }
            if(TextUtils.isEmpty(email.getText().toString().trim())){
                email.setError("field is empty");
                return;
            }
            if(!isValidEmail(str_email)){
                email.setError("enter correct email");
                return;
            }
            if(TextUtils.isEmpty(password.getText().toString())){
                password.setError("field is empty");
                return;
            }
            if(TextUtils.isEmpty(retype_password.getText().toString())){
                retype_password.setError("field is empty");
                return;
            }
            if (!TextUtils.isEmpty(password.getText().toString().trim()) && !TextUtils.isEmpty(str_retype_password.trim())){
                if(password.getText().toString().length()<8) {
                    password.setError("length is less than 8");
                    return;
                }
                if (!str_password.equals(str_retype_password)){ ///use .equals functon rather than comparing
                    Toast.makeText(this.getActivity(),"passwords dont match",Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            Log.d(TAG,"heloo its working");
            //ether u can make evry if with return so next code doesnt work f any if fails to pass or you can put all is statemnts (validating code) in 1 function and then put else for work that has to be done on click of button


        }

    }
    public static boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }
}
