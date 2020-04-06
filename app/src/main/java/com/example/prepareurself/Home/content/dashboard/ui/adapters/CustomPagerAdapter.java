package com.example.prepareurself.Home.content.dashboard.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.prepareurself.R;

public class CustomPagerAdapter extends PagerAdapter {

    private String[] data;

    private Context context;
    private LayoutInflater layoutInflater;
    private int customPosition = 0;

    public CustomPagerAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (customPosition>data.length-1)
            customPosition = 0;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =  layoutInflater.inflate(R.layout.swipe_layout,container,false);
        TextView textView = itemView.findViewById(R.id.tv_swipe);

        String text = data[customPosition];
        textView.setText(text);
        customPosition++;

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
