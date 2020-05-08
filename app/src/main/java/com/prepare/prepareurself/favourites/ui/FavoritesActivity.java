package com.prepare.prepareurself.favourites.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
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

        tvTopVideo.setText("Projects");
        tvTopTheory.setText("Resources");

        title.setText("Favourites");

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(LikedProjectFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(LikedResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);

        prefManager = new PrefManager(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.dark_grey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.dark_grey));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvTopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });

        tvTopTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });

    }
}
