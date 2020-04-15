package com.prepare.prepareurself.quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;

import java.util.HashMap;

public class OptionRecyclerViewAdapter extends RecyclerView.Adapter<OptionRecyclerViewAdapter.OptionViewHolder> {

    Context context;
    HashMap<Integer, String> options;
    int selectedPosition = -1;
    boolean selected = false;
    OptionInteractor interactor;

    public OptionRecyclerViewAdapter(Context context, HashMap<Integer, String> options, OptionInteractor interactor) {
        this.context = context;
        this.options = options;
        this.interactor = interactor;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_rv_layout,parent,false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionViewHolder holder, final int position) {
        holder.tvOption.setText(options.get(position+1));
        holder.relImage.setBackground(context.getResources().getDrawable(R.drawable.quiz_option_background));
        holder.relImage.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected){
                    selectedPosition = position;
                    notifyDataSetChanged();
                    selected = true;
                    interactor.onItemSelected(position);
                }
            }
        });

        if (selectedPosition == position){
            holder.relImage.setBackground(context.getResources().getDrawable(R.drawable.quiz_selected_option_background));
            holder.relImage.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relImage, relOption;
        ImageView imageView;
        TextView tvOption;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            relImage = itemView.findViewById(R.id.rel_img_option);
            relOption = itemView.findViewById(R.id.rel_option_quiz);
            imageView = itemView.findViewById(R.id.img_option);
            tvOption = itemView.findViewById(R.id.tv_quiz_option);
        }
    }

    public interface OptionInteractor{
        void onItemSelected(int position);
    }



}
