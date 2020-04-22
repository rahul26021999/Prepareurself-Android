package com.prepare.prepareurself.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.adapters.AuthenticationPagerAdapter;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

public class AuthenticationActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);
        if (prefManager.getBoolean(Constants.ISLOGGEDIN)){
            Intent intent=new Intent(AuthenticationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        if (getIntent().getData()!=null)
            Log.d("deeplink_debug","authentication avtivity : "+getIntent().getData()+"");

        ViewPager viewPager=findViewById(R.id.viewPager);
        AuthenticationPagerAdapter pagerAdapter =new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAndroidToken();
    }

    private void getAndroidToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM_debug", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token =  task.getResult().getToken();
                        prefManager.saveString(Constants.ANDROIDTOKEN,token);

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FCM Token", token);
//                        Toast.makeText(AuthenticationActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
