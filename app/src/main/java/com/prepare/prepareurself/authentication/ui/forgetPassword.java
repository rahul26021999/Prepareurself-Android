package com.prepare.prepareurself.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.ForgotPasswordResponseModel;
import com.prepare.prepareurself.authentication.viewmodel.AuthViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

public class forgetPassword extends AppCompatActivity {
    EditText registeredEmail  ;
    Button resetBtn;
    TextView textView;
    private AuthViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        registeredEmail=findViewById(R.id.et_email);
        textView=findViewById(R.id.textview);
        resetBtn=findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registeredEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    registeredEmail.setError(Constants.CANNOTBEEMPTY);
                    return;
                }

                if (!Utility.isValidEmail(email)){
                    registeredEmail.setError(Constants.INVALIDEMAIL);
                    return;
                }

                resetBtn.setText("Submitting");

                registeredEmail.setError(null);
                //
                viewModel.forgotPassword(email).observe(forgetPassword.this, new Observer<ForgotPasswordResponseModel>() {
                    @Override
                    public void onChanged(ForgotPasswordResponseModel forgotPasswordResponseModel) {
                        if (forgotPasswordResponseModel!=null){
                            if (forgotPasswordResponseModel.getError_code() == 0){
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Please go and check your email");
                                //submtted ardo
                            }else{
                                textView.setVisibility(View.VISIBLE);
                                textView.setText(forgotPasswordResponseModel.getMessage()); //emal not succeful le emal doesnot exst
                            }
                        }else{
                            textView.setVisibility(View.GONE);
                            Utility.showToast(forgetPassword.this,Constants.SOMETHINGWENTWRONG); //nternet connctn
                        }
                        resetBtn.setText("Submitted");
                    }

                });
            }

        });

    }

}
