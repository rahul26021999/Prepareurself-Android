package com.prepare.prepareurself.dashboard.ui.fragment;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity;
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

    private static final String TAG = "ToolbarFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;
    private RecyclerView searchRv;
    private EditText searchEdit;
    private ImageView closeSearch;

    private SearchAdapter adapter;
    private LinearLayout linMainDasboard;
    private RelativeLayout relSearchParent;
    private ProgressBar searchProgressBar;
    private RelativeLayout notFoundSearch;

    //search
    private Boolean isScrolling = false;
    private int rvCurrentItemsSearch, rvTotalItemsSearch, rvScrolledOutItemsSearch, rvLastPage, rvCurrentPageSearch =0;
    private LinearLayoutManager searchLayoutManager;

    private List<SearchRecyclerviewModel> searchRecyclerviewModels = new ArrayList<>();
    private List<String> headings = new ArrayList<>();


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
        searchRv = view.findViewById(R.id.searchcontentrv);

        viewContactsBar =  view.findViewById(R.id.viewContactsToolbar);
        searchBar =  view.findViewById(R.id.searchToolbar);
        linMainDasboard = view.findViewById(R.id.lin_main_dashboard);
        relSearchParent = view.findViewById(R.id.rel_search_parent);
        notFoundSearch = view.findViewById(R.id.not_found_view);
        searchProgressBar = view.findViewById(R.id.progress_bar_search);

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

    // Initiate toggle (it means when you click the search icon it pops up the editText and clicking the back button goes to the search icon again)
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

    // Sets the appbar state for either search mode or standard mode.
    private void setAppBaeState(int state) {

        Log.d(TAG, "setAppBaeState: changing app bar state to: " + state);

        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            searchRv.setVisibility(View.GONE);
            relSearchParent.setVisibility(View.GONE);
            linMainDasboard.setVisibility(View.VISIBLE);

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
            relSearchParent.setVisibility(View.VISIBLE);
            linMainDasboard.setVisibility(View.GONE);
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // make keyboard popup

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setAppBaeState(STANDARD_APPBAR);
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

        adapter = new SearchAdapter(getActivity(), this);
        searchLayoutManager=  new LinearLayoutManager(getActivity());
        searchRv.setLayoutManager(searchLayoutManager);
//        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),R.drawable.theory_resource_divider);
//        searchRv.addItemDecoration(decoration);
        searchRv.setAdapter(adapter);
        //searchRv.setNestedScrollingEnabled(true);

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
                    performSearch();
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

//        DashboardRecyclerviewModel dashboardRecyclerviewModel = new DashboardRecyclerviewModel(Constants.COURSEVIEWTYPE, Constants.TECHSTACK, mViewModel.getFiveCourses());
//        dashboardRecyclerviewModelList.add(dashboardRecyclerviewModel);
//        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
//        dashboardRvAdapter.notifyDataSetChanged();
//
//        DashboardRecyclerviewModel topisDashboardModel = new DashboardRecyclerviewModel(Constants.TOPICVIEWTYPE, mViewModel.getSuggestedTopics(prefManager.getString(Constants.JWTTOKEN)), Constants.TOPICSYOUMAYLIKE);
//        dashboardRecyclerviewModelList.add(topisDashboardModel);
//        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
//        dashboardRvAdapter.notifyDataSetChanged();
//
//        DashboardRecyclerviewModel projectDashboardModel = new DashboardRecyclerviewModel(mViewModel.getSuggestedProjects(prefManager.getString(Constants.JWTTOKEN)), Constants.PROJECTVIEWTYPE, Constants.PROJECTSYOUMAYLIKE);
//        dashboardRecyclerviewModelList.add(projectDashboardModel);
//        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
//        dashboardRvAdapter.notifyDataSetChanged();

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

    private void performSearch(){

        final String query = searchEdit.getText().toString();
        if (!TextUtils.isEmpty(query)){
            searchProgressBar.setVisibility(View.VISIBLE);
            notFoundSearch.setVisibility(View.GONE);

            searchRecyclerviewModels.clear();
            headings.clear();
            rvCurrentPageSearch = 0;

         //   loadMoreItems(true, query);

//            searchRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                        isScrolling = true;
//                    }
//                }
//
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    rvCurrentItemsSearch = searchLayoutManager.getChildCount();
//                    rvTotalItemsSearch = searchLayoutManager.getItemCount();
//                    rvScrolledOutItemsSearch = searchLayoutManager.findFirstVisibleItemPosition();
//
//                    // rvLastPage = topicsResponseModel.getLast_page();
//
//                    Log.d("paging_debug", rvCurrentPageSearch +","+rvLastPage);
//
//                    if (isScrolling && (rvCurrentItemsSearch + rvScrolledOutItemsSearch) == rvTotalItemsSearch){
//                        isScrolling = false;
//                        loadMoreItems(false, query);
//                    }
//
//                }
//            });

            mViewModel.search(prefManager.getString(Constants.JWTTOKEN), query).observe(getActivity(), new Observer<SearchResponseModel>() {
                @Override
                public void onChanged(SearchResponseModel searchResponseModel) {

                    if (searchResponseModel!=null){
                        List<SearchModel> searchModels = searchResponseModel.getData();
                        if (searchModels!=null){
                            Log.d("paging_debug", searchModels+" : this is id");
                            for (SearchModel searchModel : searchModels){
                                if (searchModel.getTopics()!=null){

                                    if (!headings.contains("Topics")){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel("Topics", 1));
                                        headings.add("Topics");
                                    }
                                    for (TopicsModel topicsModel : searchModel.getTopics()){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel(topicsModel,2));
                                    }

                                }else if (searchModel.getProjects()!=null){

                                    if (!headings.contains("Projects")){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel("Projects", 1));
                                        headings.add("Projects");
                                    }
                                    for (ProjectsModel topicsModel : searchModel.getProjects()){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel(topicsModel,3));
                                    }
                                }else if (searchModel.getResource()!=null){
                                    if (!headings.contains("Resources")){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel("Resources", 1));
                                        headings.add("Resources");
                                    }
                                    for (ResourceModel topicsModel : searchModel.getResource()){
                                        searchRecyclerviewModels.add(new SearchRecyclerviewModel(topicsModel,4));
                                    }
                                }
                            }

                            searchProgressBar.setVisibility(View.GONE);
                            if (!searchRecyclerviewModels.isEmpty()){
                                Log.d("paging_debug", searchRecyclerviewModels.get(0).getHeading()+"");
                                adapter.setData(searchRecyclerviewModels);
                                adapter.notifyDataSetChanged();
                            }else{
                                notFoundSearch.setVisibility(View.VISIBLE);
                                adapter.clearData();
                            }

                        }else{
                            searchProgressBar.setVisibility(View.GONE);
                            notFoundSearch.setVisibility(View.VISIBLE);
                            adapter.clearData();
                        }
                    }
                }
            });

        }
    }

//    private void loadMoreItems(final boolean isFirstPage, String query) {
//        rvCurrentPageSearch+=1;
//        mViewModel.search(prefManager.getString(Constants.JWTTOKEN), query);
//    }

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
    public void onProjectClickedFromSearch(ProjectsModel projectsModel) {
        Intent intent = new Intent(getActivity(), ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onTopicsClickedFromSearch(TopicsModel topicsModel) {
        Intent intent = new Intent(getActivity(), ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        intent.putExtra(Constants.COURSEID, topicsModel.getCourse_id());
        startActivity(intent);
    }

    @Override
    public void onResourceClickedFromSearch(final ResourceModel resourceModel) {
        if (resourceModel.getType().equalsIgnoreCase("video")){

            if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra(Constants.VIDEOCODE, Utility.getVideoCode(resourceModel.getLink()));
                intent.putExtra(Constants.VIDEOTITLE, resourceModel.getTitle());
                intent.putExtra(Constants.VIDEODESCRIPTION, resourceModel.getDescription());
                intent.putExtra(Constants.RESOURCEID, resourceModel.getId());
                intent.putExtra(Constants.RESOURCEVIDEO, true);
                startActivity(intent);
            }else {
                Utility.redirectUsingCustomTab(getActivity(), resourceModel.getLink());

                Log.d("resource_viewed", "beforeliked : " + resourceModel.getView() + ", " + resourceModel.getTotal_views() + ", " + resourceModel.getId());
                if (resourceModel.getView() == 0) {
                    mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId())
                            .observeForever(new Observer<ResourceViewsResponse>() {
                                @Override
                                public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                    if (resourceViewsResponse.getError_code() == 0) {
                                        resourceModel.setView(1);
                                        Log.d("resource_viewed", "onliked begore: " + resourceModel.getView() + ", " + resourceModel.getTotal_views() + ", " + resourceModel.getId());
                                        resourceModel.setTotal_views(resourceModel.getTotal_views() + 1);
                                        Log.d("resource_viewed", "onliked : " + resourceModel.getView() + ", " + resourceModel.getTotal_views() + ", " + resourceModel.getId());
                                        mViewModel.saveResource(resourceModel);
                                    }
                                }
                            });
                }


            }
        }else if (resourceModel.getType().equalsIgnoreCase("theory")){
            Utility.redirectUsingCustomTab(getActivity(), resourceModel.getLink());
            if (resourceModel.getView() == 0) {
                mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId())
                        .observeForever(new Observer<ResourceViewsResponse>() {
                            @Override
                            public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                if (resourceViewsResponse.getError_code() == 0) {
                                    resourceModel.setView(1);
                                    Log.d("resource_viewed", "onliked begore: " + resourceModel.getView() + ", " + resourceModel.getTotal_views() + ", " + resourceModel.getId());
                                    resourceModel.setTotal_views(resourceModel.getTotal_views() + 1);
                                    Log.d("resource_viewed", "onliked : " + resourceModel.getView() + ", " + resourceModel.getTotal_views() + ", " + resourceModel.getId());
                                    mViewModel.saveResource(resourceModel);
                                }
                            }
                        });
            }
        }
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
