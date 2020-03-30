package com.example.prepareurself.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.prepareurself.Fragments.LoginFragment;
import com.example.prepareurself.authentication.registration.view.RegisterFragment;
import com.example.prepareurself.R;
import com.example.prepareurself.adapters.AuthenticationPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager=findViewById(R.id.viewPager);
        AuthenticationPagerAdapter pagerAdapter =new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);


    }
}
