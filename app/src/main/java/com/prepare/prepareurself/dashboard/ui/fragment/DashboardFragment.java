package com.prepare.prepareurself.dashboard.ui.fragment;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.android.material.appbar.AppBarLayout;
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
import com.prepare.prepareurself.search.SearchAdapter;
import com.prepare.prepareurself.search.SearchModel;
import com.prepare.prepareurself.search.SearchRecyclerviewModel;
import com.prepare.prepareurself.search.SearchResponseModel;
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
        SliderAdapter.SliderListener,
        SearchAdapter.SearchListener {

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

    private static final String TAG = "ToolbarFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;
    private RecyclerView searchRv;
    private EditText searchEdit;
    private ImageView searchImage;


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
        searchRv = view.findViewById(R.id.searchcontentrv);

        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);

        Log.d(TAG, "onCreateView: started");

        setAppBaeState(STANDARD_APPBAR);


        ImageView ivSearchContact = view.findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked searched icon");
                toggleToolBarState();
            }
        });

        ImageView ivBackArrow = view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow.");
                toggleToolBarState();

            }
        });

        searchEdit = view.findViewById(R.id.etSearch);

        searchImage = view.findViewById(R.id.seacrh_image);



        setGoogleAdd();

        return view;
    }

    // Initiate toggle (it means when you click the search icon it pops up the editText and clicking the back button goes to the search icon again)
    private void toggleToolBarState() {
        Log.d(TAG, "toggleToolBarState: toggling AppBarState.");
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR);
        } else {
            setAppBaeState(STANDARD_APPBAR);
        }
    }

    // Sets the appbar state for either search mode or standard mode.
    private void setAppBaeState(int state) {

        Log.d(TAG, "setAppBaeState: changing app bar state to: " + state);

        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            searchRv.setVisibility(View.GONE);

            View view = getView();
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0); // make keyboard hide
            } catch (NullPointerException e) {
                Log.d(TAG, "setAppBaeState: NullPointerException: " + e);
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);
            searchRv.setVisibility(View.VISIBLE);
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // make keyboard popup

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBaeState(STANDARD_APPBAR);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        final SearchAdapter adapter = new SearchAdapter(getActivity(), this);
        searchRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchRv.setAdapter(adapter);


        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEdit.getText().toString();
                if (!TextUtils.isEmpty(query)){
                    mViewModel.search(prefManager.getString(Constants.JWTTOKEN), query)
                            .observe(getActivity(), new Observer<SearchResponseModel>() {
                                @Override
                                public void onChanged(SearchResponseModel searchResponseModel) {
                                    if (searchResponseModel!=null){
                                        List<SearchRecyclerviewModel> searchRecyclerviewModels = new ArrayList<>();
                                        if (searchResponseModel.getData()!=null && !searchResponseModel.getData().isEmpty()){
                                            for (int i = 0; i< searchResponseModel.getData().size(); i++){
                                                SearchModel searchModel = searchResponseModel.getData().get(i);
                                                if (searchModel!=null){

                                                    if (searchModel.getTopics()!=null){
                                                        SearchRecyclerviewModel searchRecyclerviewModel = new SearchRecyclerviewModel(1,"Topics",searchModel.getTopics());
                                                        searchRecyclerviewModels.add(searchRecyclerviewModel);
                                                    }else if (searchModel.getProjects()!=null){
                                                        SearchRecyclerviewModel searchRecyclerviewModel = new SearchRecyclerviewModel(searchModel.getProjects(),"Projects", 2);
                                                        searchRecyclerviewModels.add(searchRecyclerviewModel);
                                                    }else if (searchModel.getResources()!=null){
                                                        SearchRecyclerviewModel searchRecyclerviewModel = new SearchRecyclerviewModel(3, searchModel.getResources(),"Resources");
                                                        searchRecyclerviewModels.add(searchRecyclerviewModel);
                                                    }
                                                }
                                            }

                                            adapter.setData(searchRecyclerviewModels);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                }
            }
        });




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
        intent.putExtra(Constants.SHOWPAGE,Constants.SHOWTOPICS);
        startActivity(intent);
    }

    @Override
    public void onProjectSeeAll(int courseId, String courseName) {
        Intent intent = new Intent(getActivity(), CoursesActivity.class);
        intent.putExtra(Constants.COURSEID,courseId);
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
