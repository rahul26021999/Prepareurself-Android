package com.prepare.prepareurself.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Utility;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<BannerModel> mSliderItems = new ArrayList<>();
    private SliderListener listener;

    public SliderAdapter(Context context, SliderListener listener) {
        this.context = context;
        this.listener = listener;
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
        final BannerModel courseModel = mSliderItems.get(position);

        if (courseModel.getImage_url()!=null && courseModel.getImage_url().endsWith(".svg")){
            Utility.loadSVGImage(context, courseModel.getImage_url(), viewHolder.imageView);
        }else{
            Glide.with(context)
                    .load(courseModel.getImage_url())
                    .into(viewHolder.imageView);
        }
        viewHolder.textView.setText(courseModel.getTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBannerClicked(courseModel);
            }
        });

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

    public interface SliderListener{
        void onBannerClicked(BannerModel bannerModel);
    }

}
