package com.prepare.prepareurself.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;

public class VerifyEmailActivity extends BaseActivity {

    private TextView textView;
    private TextView title;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        textView = findViewById(R.id.tv_verify);
        title = findViewById(R.id.title);
        back = findViewById(R.id.backBtn);

        title.setText("Verify Email");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String email = "";

        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.USEREMAIL)!=null){
            email = intent.getStringExtra(Constants.USEREMAIL);
        }

        textView.setText("A verification link has been sent to "+email+"\n Please go and verify first\n Please check spam folder as well");



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
