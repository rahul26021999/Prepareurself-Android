package com.prepare.prepareurself.favourites.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.utils.PrefManager;

public class FavoritesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory,title;
    private ImageView BackBtn;

    private FavouritesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        viewPager = findViewById(R.id.view_pager_resources);
        tvTopVideo = findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = findViewById(R.id.tv_resouce_heading_theory);
        title=findViewById(R.id.title);
        BackBtn=findViewById(R.id.backBtn);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(LikedProjectFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(LikedResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);

        prefManager = new PrefManager(this);
    }
}
