package com.prepare.prepareurself.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;

public class VerifyEmailActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        textView = findViewById(R.id.tv_verify);

        String email = "";

        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.USEREMAIL)!=null){
            email = intent.getStringExtra(Constants.USEREMAIL);
        }

        textView.setText("A verification link has been sent to "+email+"\n Please go and verify first");



    }
}
