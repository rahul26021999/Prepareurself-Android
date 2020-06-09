package com.prepare.prepareurself.dashboard.ui.fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;
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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.prepare.prepareurself.Home.ui.SearchFragment;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.CourseDetailActivity;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.banner.BannerModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.HomepageData;
import com.prepare.prepareurself.dashboard.data.model.HomepageResponseModel;
import com.prepare.prepareurself.banner.SliderAdapter;
import com.prepare.prepareurself.dashboard.viewmodel.DashboardViewModel;
import com.prepare.prepareurself.dashboard.data.model.DashboardRecyclerviewModel;
import com.prepare.prepareurself.dashboard.ui.adapters.DashboardRvAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.search.models.SearchAdapter;
import com.prepare.prepareurself.search.models.SearchModel;
import com.prepare.prepareurself.search.models.SearchRecyclerviewModel;
import com.prepare.prepareurself.search.models.SearchResponseModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

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

    private static final String TAG = "ToolbarFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;

    private EditText searchEdit;
    private ImageView closeSearch;

    private LinearLayout linMainDasboard;

    private ShimmerFrameLayout shimmerFrameLayout;
    private FrameLayout frameLayout;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                listener.onBarClicked();
                break;
            case R.id.closeSearch:
                searchEdit.setText("");
                break;
        }
    }


    public interface HomeActivityInteractor{
        void onCourseClicked(CourseModel courseModel);
        void onTopicClicked(TopicsModel topicsModel);
        void onProjectClicked(ProjectsModel projectsModel);
        void onBarClicked();
        void onBannerClicked(BannerModel bannerModel);
        void onResourceCliked(ResourceModel resourceModel);
        void openSearchFragment();
        void closeSearchFragment();
        void performSearch(String query);
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
        frameLayout = view.findViewById(R.id.search_container);

        menu=view.findViewById(R.id.menu);
        prefManager = new PrefManager(getActivity());
        menu.setOnClickListener(this);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        viewContactsBar =  view.findViewById(R.id.viewContactsToolbar);
        searchBar =  view.findViewById(R.id.searchToolbar);
        linMainDasboard = view.findViewById(R.id.lin_main_dashboard);


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
        closeSearch = view.findViewById(R.id.closeSearch);
        closeSearch.setOnClickListener(this);

        return view;
    }

    private void toggleToolBarState() {
        Log.d(TAG, "toggleToolBarState: toggling AppBarState.");
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR);
            searchEdit.setPressed(true);
            searchEdit.requestFocus();
        } else {
            setAppBaeState(STANDARD_APPBAR);
        }
    }

    private void setAppBaeState(int state) {

        Log.d(TAG, "setAppBaeState: changing app bar state to: " + state);

        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            closeSearchFragment();
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
            openSearchFragment();
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // make keyboard popup

        }
    }

    private void closeSearchFragment() {

        listener.closeSearchFragment();
    }

    private void openSearchFragment() {
        listener.openSearchFragment();
    }



    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mAppBarState == SEARCH_APPBAR){
                    toggleToolBarState();
                }else{
                    getActivity().finish();
                }

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        mViewModel.fetchBanners(prefManager.getString(Constants.JWTTOKEN));
        mViewModel.getCourses(prefManager.getString(Constants.JWTTOKEN));

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    closeSearch.setVisibility(View.VISIBLE);
                }
                else{
                    closeSearch.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listener.performSearch(searchEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });

        dashboardRvAdapter = new DashboardRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dashboardRvAdapter);

        dashboardRecyclerviewModelList = new ArrayList<>();

        setUpSlider();

        mViewModel.fetchHomePageData(prefManager.getString(Constants.JWTTOKEN), getActivity())
                .observe(getActivity(), new Observer<HomepageResponseModel>() {
                    @Override
                    public void onChanged(HomepageResponseModel homepageResponseModel) {
                        if (homepageResponseModel!=null){
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            for (HomepageData homepageData : homepageResponseModel.getData()){
                                switch (homepageData.getType()){
                                    case "course":
                                        DashboardRecyclerviewModel d1 = new DashboardRecyclerviewModel(Constants.COURSEVIEWTYPE,homepageData.getTitle(),homepageData.isSeeAll(),homepageData.getCourses());
                                        Log.d("home_debug","d1 : "+d1.getCategoryName());
                                        dashboardRecyclerviewModelList.add(d1);
                                        break;
                                    case "project":
                                        DashboardRecyclerviewModel d2 = new DashboardRecyclerviewModel(homepageData.getProject(), Constants.PROJECTVIEWTYPE, homepageData.getTitle(), homepageData.isSeeAll(),homepageData.isViews(),homepageData.isPostedOn(),homepageData.isLikes());
                                        Log.d("home_debug","d1 : "+d2.getCategoryName());
                                        dashboardRecyclerviewModelList.add(d2);
                                        break;
                                    case "resource":
                                        DashboardRecyclerviewModel d3 = new DashboardRecyclerviewModel(Constants.RESOURCEVIEWTYPE,homepageData.getTitle(), homepageData.getResource(),homepageData.isSeeAll(),homepageData.isViews(),homepageData.isPostedOn(),homepageData.isLikes());
                                        Log.d("home_debug","d1 : "+d3.getCategoryName());
                                        dashboardRecyclerviewModelList.add(d3);
                                        break;
                                    case "topic":
                                        DashboardRecyclerviewModel d4 = new DashboardRecyclerviewModel(Constants.TOPICVIEWTYPE,homepageData.getTopics(),homepageData.getTitle(), homepageData.isSeeAll());
                                        Log.d("home_debug","d1 : "+d4.getCategoryName());
                                        dashboardRecyclerviewModelList.add(d4);
                                        break;
                                    case "ads":
                                        DashboardRecyclerviewModel d5 = new DashboardRecyclerviewModel(Constants.ADDVIEWTYPE,"Add");
                                        dashboardRecyclerviewModelList.add(d5);
                                        break;
                                }
                            }

                            dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
                            dashboardRvAdapter.notifyDataSetChanged();

                        }
                    }
                });
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
       /* Intent intent1 = new Intent(getActivity(), CourseDetailActivity.class);
        intent1.putExtra(Constants.COURSEID,courseModel.getId());
        startActivity(intent1);
        Log.d("COURSEID",""+courseModel.getId());*/
    }

    @Override
    public void onTopicClicked(TopicsModel topicsModel) {
        listener.onTopicClicked(topicsModel);
    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
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
    public void onResourceClicked(ResourceModel resourceModel) {
        listener.onResourceCliked(resourceModel);
    }

    @Override
    public void onResourceSeeAllClicked(int course_topic_id) {
        Intent intent = new Intent(getActivity(), ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID, course_topic_id);
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
