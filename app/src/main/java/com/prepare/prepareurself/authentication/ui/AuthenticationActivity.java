package com.prepare.prepareurself.authentication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.adapters.AuthenticationPagerAdapter;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

public class AuthenticationActivity extends BaseActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener {

    private PrefManager prefManager;
    public static int resourceId = -1;
    public static int projectId = -1;
    public static boolean FEEDBACKSHARE = false;
    public static boolean PROFILESHARE = false;
    private  ViewPager viewPager;

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

        resourceId = getIntent().getIntExtra(Constants.RESOURCEID, -1);

        projectId = getIntent().getIntExtra(Constants.PROJECTID, -1);

        FEEDBACKSHARE = getIntent().getBooleanExtra(Constants.FEEDBACKSHARE, false);

        PROFILESHARE = getIntent().getBooleanExtra(Constants.PROFILESHARE, false);

        viewPager=findViewById(R.id.viewPager);
        AuthenticationPagerAdapter pagerAdapter =new AuthenticationPagerAdapter(getSupportFragmentManager());
        LoginFragment loginFragment= new LoginFragment();
        pagerAdapter.addFragment(loginFragment);
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onLoginArrowClicked() {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onRegisterArrowClicked() {
        viewPager.setCurrentItem(0, true);
    }
}
