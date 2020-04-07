package com.example.prepareurself.Home.content.courses.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prepareurself.Home.content.courses.model.TheoryResources;
import com.example.prepareurself.R;

import org.w3c.dom.Text;

import java.util.List;

public class TheoryResourcesRvAdapter extends RecyclerView.Adapter<TheoryResourcesRvAdapter.TheoryResourcesViewHolder> {
    Context context;
    List<TheoryResources> theoryResources;

    public void setTheoryResources(List<TheoryResources> theoryResources) {
        this.theoryResources = theoryResources;
    }


    public TheoryResourcesRvAdapter(Context context){
        this.context=context;
    }


    @NonNull
    @Override
    public TheoryResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.theory_resources_rv_adapter_layout,parent,false);
        return new TheoryResourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheoryResourcesViewHolder holder, int position) {
        TheoryResources theoryResources1=theoryResources.get(position);
        holder.bindview(theoryResources1);
    }

    @Override
    public int getItemCount() {
        return theoryResources.size();
    }

    class TheoryResourcesViewHolder extends  RecyclerView.ViewHolder{
        ImageView theoryimageView;
        TextView theoryTvTitle;
        public TheoryResourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            theoryimageView=itemView.findViewById(R.id.img_theory_resources_adapter);
             theoryTvTitle = itemView.findViewById(R.id.tv_theory_resources_title);
        }

        public  void bindview(TheoryResources theoryResources1){
            Glide.with(context).load(
                    theoryResources1.getImageUrl()).centerCrop()
                    .placeholder(R.drawable.person_placeholder)
                    .into(theoryimageView);
            theoryTvTitle.setText(theoryResources1.getTheoryTitle());

        }

    }


}
