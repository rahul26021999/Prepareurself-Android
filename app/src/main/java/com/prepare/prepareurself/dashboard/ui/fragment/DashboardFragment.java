package com.prepare.prepareurself.dashboard.ui.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.dashboard.data.model.BannerModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.ui.adapters.SliderAdapter;
import com.prepare.prepareurself.dashboard.viewmodel.DashboardViewModel;
import com.prepare.prepareurself.dashboard.data.model.DashboardRecyclerviewModel;
import com.prepare.prepareurself.dashboard.ui.adapters.CustomPagerAdapter;
import com.prepare.prepareurself.dashboard.ui.adapters.DashboardRvAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.FixedSpeedScroller;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.ZoomPageOutTransaformer;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment implements DashboardRvAdapter.DashBoardInteractor,
        View.OnClickListener,
        SliderAdapter.SliderListener {

    private DashboardViewModel mViewModel;
    private ImageView menu;
    private RecyclerView recyclerView;
    private DashboardRvAdapter dashboardRvAdapter;

    private HomeActivityInteractor listener;
    private PrefManager prefManager;
    private int preferredCourseId = 1;
    List<DashboardRecyclerviewModel> dashboardRecyclerviewModelList;
    private SliderView sliderView;
    SliderAdapter sliderAdapter;
    private AdView mAdView;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                listener.onBarClicked();
                break;
        }
    }


    public interface HomeActivityInteractor{
        void onCourseClicked(CourseModel courseModel);
        void onTopicClicked(SuggestedTopicsModel topicsModel);
        void onProjectClicked(SuggestedProjectModel projectsModel);
        void onBarClicked();
        void onBannerClicked(BannerModel bannerModel);
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);

        recyclerView = view.findViewById(R.id.rv_main_dashboard);
        sliderView =view.findViewById(R.id.imageSlider);
        menu=view.findViewById(R.id.menu);
        prefManager = new PrefManager(getActivity());
        menu.setOnClickListener(this);
        mAdView = view.findViewById(R.id.adView);

        setGoogleAdd();

        return view;
    }

    private void setGoogleAdd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);


        mViewModel.fetchBanners(prefManager.getString(Constants.JWTTOKEN));
        mViewModel.getCourses(prefManager.getString(Constants.JWTTOKEN));


        dashboardRvAdapter = new DashboardRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dashboardRvAdapter);

        dashboardRecyclerviewModelList = new ArrayList<>();

        setUpSlider();

        DashboardRecyclerviewModel dashboardRecyclerviewModel = new DashboardRecyclerviewModel(Constants.COURSEVIEWTYPE, Constants.TECHSTACK, mViewModel.getFiveCourses());
        dashboardRecyclerviewModelList.add(dashboardRecyclerviewModel);
        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
        dashboardRvAdapter.notifyDataSetChanged();

        DashboardRecyclerviewModel topisDashboardModel = new DashboardRecyclerviewModel(Constants.TOPICVIEWTYPE, mViewModel.getSuggestedTopics(prefManager.getString(Constants.JWTTOKEN)), Constants.TOPICSYOUMAYLIKE);
        dashboardRecyclerviewModelList.add(topisDashboardModel);
        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
        dashboardRvAdapter.notifyDataSetChanged();

        DashboardRecyclerviewModel projectDashboardModel = new DashboardRecyclerviewModel(mViewModel.getSuggestedProjects(prefManager.getString(Constants.JWTTOKEN)), Constants.PROJECTVIEWTYPE, Constants.PROJECTSYOUMAYLIKE);
        dashboardRecyclerviewModelList.add(projectDashboardModel);
        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
        dashboardRvAdapter.notifyDataSetChanged();

//        mViewModel.getUserInfo().observe(getActivity(), new Observer<UserModel>() {
//            @Override
//            public void onChanged(UserModel userModel) {
//
//                if (userModel.getPreferences() != null) {
//                    preferredCourseId = Integer.parseInt(userModel.getPreferences().split(",")[0]);
//                } else {
//                    preferredCourseId = 1;
//                }
//
//                mViewModel.getFiveTopicsByCourseIdFromRemote(prefManager.getString(Constants.JWTTOKEN), preferredCourseId);
//                mViewModel.getFiveProjectsByCourseIdFromRemote(prefManager.getString(Constants.JWTTOKEN), preferredCourseId);
//
//
//            }
//        });




    }

    private void setUpSlider() {
        sliderAdapter = new SliderAdapter(getActivity(), this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        mViewModel.getBanners().observe(getActivity(), new Observer<List<BannerModel>>() {
            @Override
            public void onChanged(List<BannerModel> bannerModels) {

                if (bannerModels!=null){
                    sliderAdapter.setmSliderItems(bannerModels);
                    sliderAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onBannerClicked(BannerModel bannerModel) {
        listener.onBannerClicked(bannerModel);
    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        listener.onCourseClicked(courseModel);
    }

    @Override
    public void onTopicClicked(SuggestedTopicsModel topicsModel) {
        listener.onTopicClicked(topicsModel);
    }

    @Override
    public void onProjectClicked(SuggestedProjectModel projectsModel) {
        listener.onProjectClicked(projectsModel);
    }

    @Override
    public void onTopicSeeAll(int courseId, String courseName) {
        Intent intent = new Intent(getActivity(), CoursesActivity.class);
        intent.putExtra(Constants.COURSEID,courseId);
        intent.putExtra(Constants.COURSENAME, courseName);
        intent.putExtra(Constants.SHOWPAGE,Constants.SHOWTOPICS);
        startActivity(intent);
    }

    @Override
    public void onProjectSeeAll(int courseId, String courseName) {
        Intent intent = new Intent(getActivity(), CoursesActivity.class);
        intent.putExtra(Constants.COURSEID,courseId);
        intent.putExtra(Constants.COURSENAME, courseName);
        intent.putExtra(Constants.SHOWPAGE,Constants.SHOWPROJECTS);
        startActivity(intent);
    }

    @Override
    public void onCourseSeeAll() {
        startActivity(new Intent(getActivity(), AllCoursesActivity.class));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HomeActivityInteractor) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement HomeActivityInteractor");
        }

    }
}
