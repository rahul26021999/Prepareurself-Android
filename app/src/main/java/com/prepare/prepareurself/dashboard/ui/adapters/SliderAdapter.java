package com.prepare.prepareurself.dashboard.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.dashboard.data.model.BannerModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<BannerModel> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void setmSliderItems(List<BannerModel> mSliderItems) {
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout,parent,false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        BannerModel courseModel = mSliderItems.get(position);

        if (courseModel.getImage_url()!=null && courseModel.getImage_url().endsWith(".svg")){
            Utility.loadSVGImage(context, courseModel.getImage_url(), viewHolder.imageView);
        }else{
            Glide.with(context)
                    .load(courseModel.getImage_url())
                    .into(viewHolder.imageView);
        }
        viewHolder.textView.setText(courseModel.getTitle());
    }

    @Override
    public int getCount() {
        if (mSliderItems!=null){
            return mSliderItems.size();
        }else {
            return 0;
        }
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView;
        TextView textView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_item_image);
            textView = itemView.findViewById(R.id.slidet_item_text);
        }


    }
}
