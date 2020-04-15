package com.prepare.prepareurself.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.prepare.prepareurself.authentication.ui.LoginFragment;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.authentication.ui.RegisterFragment;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.adapters.AuthenticationPagerAdapter;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

public class MainActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);
        if (prefManager.getBoolean(Constants.ISLOGGEDIN)){
            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        ViewPager viewPager=findViewById(R.id.viewPager);
        AuthenticationPagerAdapter pagerAdapter =new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);



    }
}
