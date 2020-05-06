package com.prepare.prepareurself.favourites.ui;

import android.os.Bundle;

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
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

public class FavouritesFragment extends Fragment {

    private ViewPager viewPager;
    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory,title;
    private ImageView BackBtn;

    private FavouritesViewModel viewModel;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance(String param1, String param2) {
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

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(LikedProjectFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(LikedResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);

        prefManager = new PrefManager(getActivity());

      //  viewModel.fetchFavourites(prefManager.getString(Constants.JWTTOKEN),10, 1);

    }
}
