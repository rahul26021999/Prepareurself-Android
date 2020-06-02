package com.prepare.prepareurself.courses.ui.fragmentToActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.adapters.ResourcesRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.TopicViewModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.GridSpaceItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class TabResourceActivity extends AppCompatActivity implements ResourcesRvAdapter.ResourceRvInteractor {
    private TopicViewModel mViewModel;
    private RecyclerView recyclerView;
    private ResourcesRvAdapter adapter;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private PrefManager prefManager;
    private TextView tvComingSoon;
    private int courseId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_resource);
        recyclerView= findViewById(R.id.rv_resources);
        tvComingSoon=findViewById(R.id.tv_coming_soon);
        mViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        prefManager = new PrefManager(this);

        adapter = new ResourcesRvAdapter(this, this);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(30));
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID,-1);

        Log.d("course_id", courseId+"");
        if (courseId!=-1){
            mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                    courseId,
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }

        mViewModel.getTopicsResponseModelLiveData().observe(this, new Observer<TopicsResponseModel>() {
            @Override
            public void onChanged(final TopicsResponseModel topicsResponseModel) {
                if (topicsResponseModel!=null){
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            rvCurrentItems = layoutManager.getChildCount();
                            rvTotalItems = layoutManager.getItemCount();
                            rvScrolledOutItems = layoutManager.findFirstVisibleItemPosition();

                            rvLastPage = topicsResponseModel.getLast_page();

                            Log.d("paging_debug",rvCurrentPage+","+rvLastPage);

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage<=rvLastPage){
                                isScrolling = false;
                                mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                                        courseId,
                                        10,
                                        rvCurrentPage);
                                rvCurrentPage+=1;
                            }

                        }
                    });
                }
            }
        });

        mViewModel.getLiveData(courseId).observe(this, new Observer<List<TopicsModel>>() {
            @Override
            public void onChanged(List<TopicsModel> topicsModels) {

                if (topicsModels!=null && !topicsModels.isEmpty()){
                    tvComingSoon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setTopics(topicsModels);
                    adapter.notifyDataSetChanged();
                }else{
                    tvComingSoon.setText("Coming Soon...");
                    tvComingSoon.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }


            }
        });
    }

    @Override
    public void onResourceClicked(TopicsModel topicsModel) {
        Intent intent =new Intent(TabResourceActivity.this, ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        intent.putExtra(Constants.COURSEID, topicsModel.getCourse_id());
        startActivity(intent);
    }
}
