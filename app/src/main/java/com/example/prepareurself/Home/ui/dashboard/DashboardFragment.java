package com.example.prepareurself.Home.ui.dashboard;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prepareurself.Home.ui.adapters.CustomPagerAdapter;
import com.example.prepareurself.R;

import org.w3c.dom.Text;

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

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        viewPager = view.findViewById(R.id.viewpager_dashboard);
        dotsContainer = view.findViewById(R.id.dotsContainer);

        customPagerAdapter = new CustomPagerAdapter(this.getActivity(),data);
        viewPager.setAdapter(customPagerAdapter);
        prepareDots(dotsPosition++);
        createSlideShow();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (dotsPosition>data.length-1)
                    dotsPosition=0;
                prepareDots(dotsPosition++);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
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
