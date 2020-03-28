package com.example.prepareurself.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class AuthenticationPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList=new  ArrayList<>();
    public AuthenticationPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

}
