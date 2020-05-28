package com.prepare.prepareurself.favourites.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.utils.PrefManager;

public class FavouritesFragment extends Fragment {

    private ViewPager viewPager;
    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory,title;
    private ImageView BackBtn;
    private FavouritesHomeInteractor listener;

    private FavouritesViewModel viewModel;
    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_resources, container, false);


        viewPager = view.findViewById(R.id.view_pager_resources);
        tvTopVideo = view.findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = view.findViewById(R.id.tv_resouce_heading_theory);
        title=view.findViewById(R.id.title);
        BackBtn=view.findViewById(R.id.backBtn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        tvTopVideo.setText("Projects");
        tvTopTheory.setText("Resources");

        title.setText("Favourites");

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                listener.onFavBackPressed();
            }
        });

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(LikedProjectFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(LikedResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);

        prefManager = new PrefManager(getActivity());

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

    public interface FavouritesHomeInteractor{
        void onFavBackPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FavouritesHomeInteractor) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FavouritesHomeInteractor");
        }

    }

}
