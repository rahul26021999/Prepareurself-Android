package com.prepare.prepareurself.dashboard.ui.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import android.widget.LinearLayout;

import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment implements DashboardRvAdapter.DashBoardInteractor {

    private DashboardViewModel mViewModel;
    private ViewPager viewPager;
    private CustomPagerAdapter customPagerAdapter;
    private Timer timer;
    private int currentPosition = 0;
    String[] data = {"Slide view 1","Slide view 2","Slide view 3","Slide view 4","Slide view 5"};
    private LinearLayout dotsContainer;
    private int dotsPosition = 0;
    private RecyclerView recyclerView;
    private DashboardRvAdapter dashboardRvAdapter;

    private HomeActivityInteractor listener;
    private PrefManager prefManager;
    private int preferredCourseId = 1;
    List<DashboardRecyclerviewModel> dashboardRecyclerviewModelList;


    public interface HomeActivityInteractor{
        void onCourseClicked(CourseModel courseModel);
        void onTopicClicked(TopicsModel topicsModel);
        void onProjectClicked(ProjectsModel projectsModel);
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        viewPager = view.findViewById(R.id.viewpager_dashboard);
        dotsContainer = view.findViewById(R.id.dotsContainer);
        recyclerView = view.findViewById(R.id.rv_main_dashboard);

        prefManager = new PrefManager(getActivity());

//        setUpViewPager();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);


        mViewModel.getCourses(prefManager.getString(Constants.JWTTOKEN));

        dashboardRvAdapter = new DashboardRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dashboardRvAdapter);

        dashboardRecyclerviewModelList = new ArrayList<>();

        DashboardRecyclerviewModel dashboardRecyclerviewModel = new DashboardRecyclerviewModel(Constants.COURSEVIEWTYPE,Constants.TECHSTACK,mViewModel.getLiveCourses());
        dashboardRecyclerviewModelList.add(dashboardRecyclerviewModel);
        dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
        dashboardRvAdapter.notifyDataSetChanged();

        mViewModel.getUserInfo().observe(getActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {


                if (userModel.getPreferences()!=null){
                    preferredCourseId = Integer.parseInt(userModel.getPreferences().split(",")[0]);
                }else{
                    preferredCourseId = 1;
                }

                mViewModel.getFiveTopicsByCourseIdFromRemote(prefManager.getString(Constants.JWTTOKEN),preferredCourseId);
                mViewModel.getFiveProjectsByCourseIdFromRemote(prefManager.getString(Constants.JWTTOKEN),preferredCourseId);

                DashboardRecyclerviewModel dashboardRecyclerviewModel = new DashboardRecyclerviewModel(Constants.TOPICVIEWTYPE,mViewModel.getFiveTopicsByCourseIdFromDb(preferredCourseId),Constants.TOPICSYOUMAYLIKE);
                dashboardRecyclerviewModelList.add(dashboardRecyclerviewModel);
                dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
                dashboardRvAdapter.notifyDataSetChanged();

                DashboardRecyclerviewModel projectDashboardModel = new DashboardRecyclerviewModel(mViewModel.getFiveProjectByCourseId(preferredCourseId),Constants.PROJECTVIEWTYPE,Constants.PROJECTSYOUMAYLIKE);
                dashboardRecyclerviewModelList.add(projectDashboardModel);
                dashboardRvAdapter.setData(dashboardRecyclerviewModelList);
                dashboardRvAdapter.notifyDataSetChanged();
            }
        });



    }

    private void setUpViewPager() {
        customPagerAdapter = new CustomPagerAdapter(this.getActivity(),data);
        viewPager.setAdapter(customPagerAdapter);

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
        }

        viewPager.setPageTransformer(true, new ZoomPageOutTransaformer());

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem()+1 == Integer.MAX_VALUE){
                    currentPosition = 0;
                }
                Log.d("viewpager_position", currentPosition+"");
                viewPager.setCurrentItem(currentPosition++,true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },5000,5000);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HomeActivityInteractor) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement HomeActivityInteractor");
        }

    }
}
