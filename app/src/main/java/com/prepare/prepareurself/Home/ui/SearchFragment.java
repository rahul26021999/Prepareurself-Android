package com.prepare.prepareurself.Home.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.dashboard.viewmodel.DashboardViewModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.search.models.SearchAdapter;
import com.prepare.prepareurself.search.models.SearchModel;
import com.prepare.prepareurself.search.models.SearchRecyclerviewModel;
import com.prepare.prepareurself.search.models.SearchResponseModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchAdapter.SearchListener {

    private ProgressBar searchProgressBar;
    private RelativeLayout notFoundSearch;
    private RecyclerView searchRv;
    private DashboardViewModel mViewModel;
    private SearchAdapter adapter;
    private PrefManager prefManager;
    private LinearLayoutManager searchLayoutManager;
    private int currentSearchPage = 1;
    private  boolean EndOfSearchData=false;
    private Boolean isScrolling = false;
    private int rvCurrentItemsSearch, rvTotalItemsSearch, rvScrolledOutItemsSearch, rvLastPage, rvCurrentPageSearch =0;

    private List<SearchRecyclerviewModel> searchRecyclerviewModels = new ArrayList<>();
    private List<String> headings = new ArrayList<>();
    private String query="";

    private Boolean shouldSearchPaginationStopped = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        prefManager = new PrefManager(getActivity());

        Log.d("search_debug_view","oncreate");

        notFoundSearch = view.findViewById(R.id.not_found_view);
        searchProgressBar = view.findViewById(R.id.progress_bar_search);
        searchRv = view.findViewById(R.id.searchcontentrv);

        adapter = new SearchAdapter(getActivity(), this);
        searchLayoutManager=  new LinearLayoutManager(getActivity());
        searchRv.setLayoutManager(searchLayoutManager);
        searchRv.setAdapter(adapter);

        return view;
    }

    public void performSearch(String q)
    {
        query=q;
        if (!TextUtils.isEmpty(query)){
            searchProgressBar.setVisibility(View.VISIBLE);
            notFoundSearch.setVisibility(View.GONE);

            Log.d("paging_debug","click on change");

            EndOfSearchData=false;
            searchRecyclerviewModels.clear();
            headings.clear();
            currentSearchPage=1;

            mViewModel.searchWithPagination(prefManager.getString(Constants.JWTTOKEN),query,currentSearchPage);

            mViewModel.searchResponseModelLiveData.observe(getActivity(), new Observer<SearchResponseModel>() {
                @Override
                public void onChanged(SearchResponseModel searchResponseModel) {

                    Log.d("paging_debug",searchResponseModel+" on change");

                    if(currentSearchPage==1){
                        searchRecyclerviewModels.clear();
                        headings.clear();
                        EndOfSearchData=false;
                    }
                    if (searchResponseModel!=null){

                        List<SearchModel> searchModels = searchResponseModel.getData();
                        if (searchModels!=null && !searchModels.isEmpty())
                        {
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
                            notFoundSearch.setVisibility(View.GONE);
                            adapter.setData(searchRecyclerviewModels);

                        }
                        else{
                            if (currentSearchPage == 1){
                                searchProgressBar.setVisibility(View.GONE);
                                notFoundSearch.setVisibility(View.VISIBLE);
                                adapter.clearData();
                                EndOfSearchData=true;
                            }else{
                                EndOfSearchData=true;
                            }
                        }
                    }
                }
            });

        }
        else{
            currentSearchPage=1;
        }
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
    public void onBottomReached(int position) {
        if(!EndOfSearchData){
            currentSearchPage++;
            Utility.showToast(getActivity(),"hello ray chutiya hai "+currentSearchPage);
            mViewModel.searchWithPagination(prefManager.getString(Constants.JWTTOKEN),query,currentSearchPage);
        }
    }
}
