package com.prepare.prepareurself.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prepare.prepareurself.aboutus.ui.AboutusFragment;
import com.prepare.prepareurself.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.favourites.ui.FavouritesFragment;
import com.prepare.prepareurself.feedback.ui.FeedbackFragment;
import com.prepare.prepareurself.profile.ui.fragments.ProfileFragment;
import com.prepare.prepareurself.trending.ui.TrendingFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private int count = 6;

    public HomeViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position){
            case 0: fragment = DashboardFragment.newInstance();
            break;
            case 1: fragment = ProfileFragment.newInstance();
            break;
            case 2: fragment = FavouritesFragment.newInstance();
            break;
            case 3: fragment = new TrendingFragment();
            break;
            case 4: fragment = FeedbackFragment.newInstance();
            break;
            case 5: fragment = AboutusFragment.newInstance();
            break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }
}
