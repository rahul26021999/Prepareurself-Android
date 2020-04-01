package com.example.prepareurself.Home.ui.dashboard;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prepareurself.Home.model.DashboardRecyclerviewModel;
import com.example.prepareurself.Home.ui.adapters.CustomPagerAdapter;
import com.example.prepareurself.Home.ui.adapters.DashboardRvAdapter;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.FixedSpeedScroller;
import com.example.prepareurself.utils.ZoomPageOutTransaformer;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment {

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

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);

        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        viewPager = view.findViewById(R.id.viewpager_dashboard);
        dotsContainer = view.findViewById(R.id.dotsContainer);
        recyclerView = view.findViewById(R.id.rv_main_dashboard);

        customPagerAdapter = new CustomPagerAdapter(this.getActivity(),data);
        viewPager.setAdapter(customPagerAdapter);
//        prepareDots(dotsPosition++);
//        createSlideShow();

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
        },2500,2500);

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position+1 == data.length){
//                    currentPosition = 0;
//                }else
//                    currentPosition = position;
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPosition = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        MutableLiveData<List<DashboardRecyclerviewModel>> modelList = mViewModel.getModelList();
        dashboardRvAdapter = new DashboardRvAdapter(modelList.getValue(), this.getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dashboardRvAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    private void createSlideShow(){

        final Handler handler = new Handler();

        final Runnable runnable =  new Runnable(){
            @Override
            public void run() {
                if (currentPosition == Integer.MAX_VALUE)
                    currentPosition = 0;

                viewPager.setCurrentItem(currentPosition++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250,2500);
    }

    private void prepareDots(int currentSlidePosition){

        TextView[] dot = new TextView[data.length];
        dotsContainer.removeAllViews();
        for (int i=0; i< dot.length; i++) {
            dot[i] = new TextView(this.getActivity());
            dot[i].setText(Html.fromHtml("&#9673;"));
            dot[i].setTextSize(8F);
            dot[i].setTextColor(this.getResources().getColor(R.color.inactive_dots));
            dotsContainer.addView(dot[i]);
        }
        //active dot
        dot[currentSlidePosition].setTextColor(this.getResources().getColor(R.color.active_dots));


    }

}
